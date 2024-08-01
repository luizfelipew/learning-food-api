package com.works.foodapi.infrastructure.service;

import com.works.foodapi.domain.filter.VendaDiariaFilter;
import com.works.foodapi.domain.model.dto.VendaDiaria;
import com.works.foodapi.domain.service.VendaQueryService;

import java.util.List;

public class VendaQueryServiceImpl implements VendaQueryService {
    @Override
    public List<VendaDiaria> consultarVendasDiarias(VendaDiariaFilter filtro) {
        return List.of();
    }
}
