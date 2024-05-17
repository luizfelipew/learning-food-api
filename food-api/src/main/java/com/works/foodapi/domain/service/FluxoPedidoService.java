package com.works.foodapi.domain.service;

import com.works.foodapi.domain.exception.NegocioException;
import com.works.foodapi.domain.model.Pedido;
import com.works.foodapi.domain.model.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class FluxoPedidoService {

    private final EmissaoPedidoService emissaoPedidoService;

    @Transactional
    public void confirmar(final String codigoPedido) {
        val pedido = emissaoPedidoService.buscarOuFalhar(codigoPedido);

        pedido.confirmar();
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
