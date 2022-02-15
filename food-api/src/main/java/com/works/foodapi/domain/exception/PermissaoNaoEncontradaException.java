package com.works.foodapi.domain.exception;

public class PermissaoNaoEncontradaException extends EntidadeNaoEncontradaException {

    public PermissaoNaoEncontradaException(Long permissaoId) {
        this(String.format("Não existe um cadastro de permissão com código %d", permissaoId));
    }

    public PermissaoNaoEncontradaException(String mensagem) {
        super(mensagem);
    }
}
