package com.works.foodapi.api.controller;

import com.works.foodapi.domain.filter.VendaDiariaFilter;
import com.works.foodapi.domain.model.dto.VendaDiaria;
import com.works.foodapi.domain.service.VendaQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/estatisticas")
public class EstatisticasController {

    private final VendaQueryService vendaQueryService;

    @GetMapping("/vendas-diarias")
    public List<VendaDiaria> consultarVendasDiarias (final VendaDiariaFilter filtro,
                                                     @RequestParam(required = false, defaultValue = "+00:00") final String timeOffset) {
        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }
}
