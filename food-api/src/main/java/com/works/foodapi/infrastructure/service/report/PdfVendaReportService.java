package com.works.foodapi.infrastructure.service.report;

import com.works.foodapi.domain.filter.VendaDiariaFilter;
import com.works.foodapi.domain.service.VendaReportService;
import org.springframework.stereotype.Service;

@Service
public class PdfVendaReportService implements VendaReportService {
    @Override
    public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
        return null;
    }
}
