package appsys.free.Finanzas.controlFinanzas.services.impls;

import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.repositories.IReportesRepo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportesImplService  {

    private static final Logger logger = LoggerFactory.getLogger(ReportesImplService.class);

    private final IReportesRepo reporteRepository;

    public List<SaldoPresupuestoDTO> getSaldoPorCategoria() {
        return reporteRepository.obtenerSaldoPresupuesto();
    }


}
