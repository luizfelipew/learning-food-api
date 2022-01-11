package com.works.foodapi.api.controller;

import com.works.foodapi.api.assembler.FormaPagamentoModelAssembler;
import com.works.foodapi.api.model.FormaPagamentoModel;
import com.works.foodapi.domain.model.Restaurante;
import com.works.foodapi.domain.service.CadastroRestauranteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/restaurantes/{restauranteId}/formas-pagamento")
@RequiredArgsConstructor
public class RestauranteFormaPagamentoController {

    private final CadastroRestauranteService cadastroRestaurante;
    private final FormaPagamentoModelAssembler formaPagamentoModelAssembler;

    @GetMapping
    public List<FormaPagamentoModel> listar(@PathVariable Long restauranteId) {
        final Restaurante restaurante = cadastroRestaurante.buscarOuFalhar(restauranteId);

        return formaPagamentoModelAssembler.toCollectionModel(restaurante.getFormasPagamento());
    }

    @DeleteMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desassociar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId ){
        cadastroRestaurante.desassociarFormaPagamento(restauranteId, formaPagamentoId);
    }

    @PutMapping("/{formaPagamentoId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void associar(@PathVariable Long restauranteId, @PathVariable Long formaPagamentoId ){
        cadastroRestaurante.associarFormaPagamento(restauranteId, formaPagamentoId);
    }

}
