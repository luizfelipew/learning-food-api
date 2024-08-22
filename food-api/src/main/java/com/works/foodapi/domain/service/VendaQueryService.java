package com.works.foodapi.domain.service;

import com.works.foodapi.domain.filter.VendaDiariaFilter;
import com.works.foodapi.domain.model.dto.VendaDiaria;

import java.util.List;

public interface VendaQueryService {

    List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro, String timeOffset);
}
