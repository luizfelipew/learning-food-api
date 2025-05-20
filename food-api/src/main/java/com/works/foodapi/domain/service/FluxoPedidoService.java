package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.NegocioException;
import com.works.foodapi.domain.model.Pedido;
import com.works.foodapi.domain.model.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.Collections;

import static com.works.foodapi.domain.service.EnvioEmailService.*;

@Service
@RequiredArgsConstructor
public class FluxoPedidoService {

    private final EmissaoPedidoService emissaoPedidoService;
    private final EnvioEmailService emailService;

    @Transactional
    public void confirmar(final String codigoPedido) {
        val pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);
        pedido.confirmar();

       var mensagem = Mensagem.builder()
               .assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
               .corpo("Pedido do código <strong>" + pedido.getCodigo() + "</strong> foi confirmado!")
               .destinatarios(Collections.singleton(pedido.getCliente().getEmail()))
               .build();

        emailService.enviar(mensagem);

    }

    @Transactional
    public void cancelar(final String codigoPedido) {
        val pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);

        pedido.cancelar();
    }

    @Transactional
    public void entregar(final String codigoPedido) {
        val pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);

        pedido.entregar();
    }
}
