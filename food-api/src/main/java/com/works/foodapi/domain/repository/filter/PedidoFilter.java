package com.works.foodapi.domain.repository.filter;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.OffsetDateTime;

import static org.springframework.format.annotation.DateTimeFormat.*;

@Setter
@Getter
public class PedidoFilter {

    private Long clienteId;
    private Long restauranteId;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime dataCriacaoInicio;
    @DateTimeFormat(iso = ISO.DATE_TIME)
    private OffsetDateTime dataCriacaoFim;
}
