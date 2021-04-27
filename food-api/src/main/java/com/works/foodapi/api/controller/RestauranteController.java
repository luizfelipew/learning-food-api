package com.works.foodapi.api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.works.foodapi.domain.exception.EntidadeNaoEncontradaException;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.repository.RestauranteRepository;
import com.works.foodapi.domain.service.CadastroRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping(value = "/restaurantes")
@RequiredArgsConstructor
public class RestauranteController {

    private final RestauranteRepository restauranteRepository;
    private final CadastroRestauranteService cadastroRestaurante;

    @GetMapping
    public List<Restaurante> listar() {
        return restauranteRepository.findAll();
    }

    @GetMapping("/{restauranteId}")
    public Restaurante buscar(@PathVariable Long restauranteId) {
        return cadastroRestaurante.buscarOuFalhar(restauranteId);
    }

//    @PostMapping
//    public ResponseEntity<?> adicionar(@RequestBody Restaurante restaurante) {
//        try {
//            restaurante = cadastroRestaurante.salvar(restaurante);
//            return ResponseEntity.status(HttpStatus.CREATED)
//                    .body(restaurante);
//        } catch (EntidadeNaoEncontradaException ex) {
//            return ResponseEntity.badRequest()
//                    .body(ex.getMessage());
//        }
//    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Restaurante adicionar(@RequestBody Restaurante restaurante) {
        return cadastroRestaurante.salvar(restaurante);
    }

//    @PutMapping("/{restauranteId}")
//    public ResponseEntity<?> atualizar(@PathVariable Long restauranteId,
//                                       @RequestBody Restaurante restaurante) {
//        Optional<Restaurante> restauranteAtual = restauranteRepository.findById(restauranteId);
//
//        if (restauranteAtual.isPresent()) {
//            BeanUtils.copyProperties(restaurante, restauranteAtual.get(), "id",
//                    "formasPagamento", "endereco", "dataCadastro", "produtos");
//            try {
//                Restaurante restauranteSalva = cadastroRestaurante.salvar(restauranteAtual.get());
//                return ResponseEntity.ok(restauranteSalva);
//            } catch (EntidadeNaoEncontradaException ex) {
//                return ResponseEntity.badRequest()
//                        .body(ex.getMessage());
//            }
//        }
//        return ResponseEntity.notFound().build();
//    }

    @PutMapping("/{restauranteId}")
    public Restaurante atualizar(@PathVariable Long restauranteId,
                                       @RequestBody Restaurante restaurante) {
        Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

        BeanUtils.copyProperties(restaurante, restauranteAtual, "id",
                "formasPagamento", "endereco", "dataCadastro", "produtos");
        return cadastroRestaurante.salvar(restauranteAtual);
    }

    @PatchMapping("/{restauranteId}")
    public Restaurante atualizarParcial(@PathVariable Long restauranteId,
                                              @RequestBody Map<String, Object> campos) {
        Restaurante restauranteAtual = cadastroRestaurante.buscarOuFalhar(restauranteId);

        merge(campos, restauranteAtual);
        return atualizar(restauranteId, restauranteAtual);
    }

    private void merge(Map<String, Object> dadosOrigem, Restaurante restauranteDestino) {
        ObjectMapper objectMapper = new ObjectMapper();
        Restaurante restauranteOrigem = objectMapper.convertValue(dadosOrigem, Restaurante.class);

        dadosOrigem.forEach((nomePropriedade, valorPropriedade) -> {
            Field field = ReflectionUtils.findField(Restaurante.class, nomePropriedade);
            field.setAccessible(true);

            Object novoValor = ReflectionUtils.getField(field, restauranteOrigem);

//            System.out.println(nomePropriedade + " = " + valorPropriedade + " = " + novoValor);

            ReflectionUtils.setField(field, restauranteDestino, novoValor);
        });
    }


}
