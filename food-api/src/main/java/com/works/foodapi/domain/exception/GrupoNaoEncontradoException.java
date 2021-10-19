package com.works.foodapi.domain.exception;

public class GrupoNaoEncontradoException extends EntidadeNaoEncontradaException {

    public GrupoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }

    public GrupoNaoEncontradoException(Long estadoId) {
        this(String.format("Não existe um cadastro de grupo com código %d", estadoId));
    }
}
