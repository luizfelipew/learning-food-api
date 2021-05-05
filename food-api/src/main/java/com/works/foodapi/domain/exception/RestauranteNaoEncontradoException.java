package com.works.foodapi.domain.exception;

public class RestauranteNaoEncontradoException extends EntidadeNaoEncontradaException {

    public RestauranteNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public RestauranteNaoEncontradoException(Long conzinhaId) {
        this(String.format("Não existe cadastro de restaurante com o código %d", conzinhaId));
    }
}
