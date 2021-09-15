package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.RestauranteInputDisassembler;
import com.works.foodapi.api.assembler.RestauranteModelAssembler;
import com.works.foodapi.api.model.RestauranteModel;
import com.works.foodapi.api.model.input.RestauranteInput;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.exception.NegocioException;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.RestauranteRepository;
import com.works.foodapi.domain.service.CadastroRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.SmartValidator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final CadastroRestauranteService cadastroRestaurante;
    private final RestauranteModelAssembler restauranteModelAssembler;
    private final RestauranteInputDisassembler restauranteInputDisassembler;
    private final SmartValidator validator;

    @GetMapping
    public List<RestauranteModel> listar() {
        return restauranteModelAssembler.toCollectionModel(restauranteRepository.findAll());
    }

    @GetMapping("/{restauranteId}")
    public RestauranteModel buscar(@PathVariable Long restauranteId) {
        final Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        return restauranteModelAssembler.toModel(restaurante);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RestauranteModel adicionar(
            @RequestBody @Valid RestauranteInput restauranteInput) {

        try {
            final Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);

            return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restaurante));
        } catch (EntidadeNaoEncontradaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

    @PutMapping("/{restauranteId}")
    public RestauranteModel atualizar(@PathVariable Long restauranteId,
                                      @RequestBody @Valid RestauranteInput restauranteInput) {
        try {
            final Restaurante restaurante = restauranteInputDisassembler.toDomainObject(restauranteInput);
            Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

            BeanUtils.copyProperties(restaurante, restauranteAtual, "id",
                    "formasPagamento", "endereco", "dataCadastro", "produtos");

            return restauranteModelAssembler.toModel(cadastroRestaurante.salvar(restauranteAtual));
        } catch (EntidadeNaoEncontradaException ex) {
            throw new NegocioException(ex.getMessage());
        }
    }

}
