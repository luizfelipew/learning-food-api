package com.works.foodapi.api.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.works.foodapi.api.model.RestauranteModel;
import com.works.foodapi.core.validation.ValidacaoException;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.exception.NegocioException;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.RestauranteRepository;
import com.works.foodapi.domain.service.CadastroRestauranteService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.util.ReflectionUtils;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final CadastroRestauranteService cadastroRestaurante;
    private final SmartValidator validator;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    @GetMapping("/{restauranteId}")
    public RestauranteModel buscar(@PathVariable Long restauranteId) {
        final Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        final RestauranteModel restauranteModel = null;
        return restauranteModel;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(
            @RequestBody @Valid Restaurante restaurante) {

        try {
            return cadastroRestaurante.salvar(restaurante);
        } catch (EntidadeNaoEncontradaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable Long restauranteId,
                                 @RequestBody @Valid Restaurante restaurante) {
        Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

        BeanUtils.copyProperties(restaurante, restauranteAtual, "id",
                "formasPagamento", "endereco", "dataCadastro", "produtos");
        try {
            return cadastroRestaurante.salvar(restauranteAtual);
        } catch (EntidadeNaoEncontradaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @PatchMapping("/{restauranteId}")
    public Restaurante atualizarParcial(@PathVariable Long restauranteId,
                                        @RequestBody Map<String, Object> campos,
                                        HttpServletRequest request) {
        Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

        merge(campos, restauranteAtual, request);

        validate(restauranteAtual, "restaurante");

        return atualizar(restauranteId, restauranteAtual);
    }

    private void validate(Restaurante restaurante, String objectName) {
        BeanPropertyBindingResult bindingResult = new BeanPropertyBindingResult(restaurante, objectName);

        validator.validate(restaurante, bindingResult);

        if (bindingResult.hasErrors()){
            throw new ValidacaoException(bindingResult);
        }
    }

    private void merge(
            Map<String, Object> dadosOrigem,
            Restaurante restauranteDestino,
            HttpServletRequest request) {

        ServletServerHttpRequest serverHttpRequest = new ServletServerHttpRequest(request);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, true);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);

            Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

            dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
                Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
                field.setAccessible(true);

                Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

//            System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);

                ReflectionUtils.setField(field, restauranteDestino, novoValor);
            });
        } catch (IllegalArgumentException ex) {
           Throwable rootCause = ExceptionUtils.getRootCause(ex);
           throw new HttpMessageNotReadableException(ex.getMessage(), rootCause, serverHttpRequest);
        }
    }


}
