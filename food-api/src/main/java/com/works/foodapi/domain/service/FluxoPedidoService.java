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
    public void confirmar(final Long pedidoId) {
        val pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

        pedido.confirmar();
    }

    @Transactional
    public void cancelar(final Long pedidoId) {
        val pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

        pedido.cancelar();
    }

    @Transactional
    public void entregar(Long pedidoId) {
        val pedido = emissaoPedidoService.buscarOuFalhar(pedidoId);

        pedido.entregar();
    }
}
