package com.works.foodapi.domain.exception;

public class CozinhaNaoEncontradaException extends EntidadeNaoEncontradaException {

    public CozinhaNaoEncontradaException(String mensagem) {
        super(mensagem);
    }

    public CozinhaNaoEncontradaException(Long conzinhaId) {
        this(String.format("Não existe um cadastro de cozinha com código %d", conzinhaId));
    }
}
