package com.works.foodapi.infrastructure.service.report;

import com.works.foodapi.domain.filter.VendaDiariaFilter;
import com.works.foodapi.domain.service.VendaQueryService;
import com.works.foodapi.domain.service.VendaReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;

@RequiredArgsConstructor
@Service
public class PdfVendaReportService implements VendaReportService {

    private final VendaQueryService vendaQueryService;

    @Override
    public byte[] emitirVendasDiarias(VendaDiariaFilter filtro, String timeOffset) {
        try {
            var inputStream = this.getClass().getResourceAsStream("/relatorios/vendas-diarias.jasper");

            var parameters = new HashMap<String, Object>();
            parameters.put("REPORT_LOCALE", new Locale("pt", "BR"));

            var vendasDiarias = vendaQueryService.consultarVendasDiarias(filtro, timeOffset);

            var dataSource = new JRBeanCollectionDataSource(vendasDiarias);

            var jasperPrint = JasperFillManager.fillReport(inputStream, parameters, dataSource);

            return JasperExportManager.exportReportToPdf(jasperPrint);
        } catch (Exception ex) {

            throw new ReportException("Não foi possivel emitir relatório de vendas diárias", ex);
        }

    }
}
