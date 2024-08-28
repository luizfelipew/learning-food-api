package com.works.foodapi.api.controller;

import com.works.foodapi.domain.filter.VendaDiariaFilter;
import com.works.foodapi.domain.model.dto.VendaDiaria;
import com.works.foodapi.domain.service.VendaQueryService;
import com.works.foodapi.domain.service.VendaReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    private final VendaReportService vendaReportService;

    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<VendaDiaria> consultarVendasDiarias(final VendaDiariaFilter filtro,
                                                    @RequestParam(required = false, defaultValue = "+00:00") final String timeOffset) {
        return vendaQueryService.consultarVendasDiarias(filtro, timeOffset);
    }

    @GetMapping(path = "/vendas-diarias", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> consultarVendasDiariasPdf(final VendaDiariaFilter filtro,
                                                            @RequestParam(required = false, defaultValue = "+00:00") final String timeOffset) {

        byte[] bytesPdf = vendaReportService.emitirVendasDiarias(filtro, timeOffset);

        var headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=vendas-diarias.pdf");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .headers(headers)
                .body(bytesPdf);
    }
}
