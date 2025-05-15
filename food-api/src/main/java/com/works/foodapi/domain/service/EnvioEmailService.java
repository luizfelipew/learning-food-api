package com.works.foodapi.domain.service;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

public interface EnvioEmailService {

    void enviar(final Mensagem mensagem);

    @Getter
    @Builder
    class Mensagem {

        private Set<String> destinatários;
        private  String assunto;
        private  String corpo;
    }
}
