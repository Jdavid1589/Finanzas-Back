package appsys.free.Finanzas.controlFinanzas.controllers;


import appsys.free.Finanzas.controlFinanzas.dtos.ReporteMensualDTO;
import appsys.free.Finanzas.controlFinanzas.dtos.ReporteResumenDTO;
import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.impls.ReporteFinancieroService;
import appsys.free.Finanzas.controlFinanzas.services.impls.ReportesImplService;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IReportesFinancieros;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class Reportes_Controller {

    private static final Logger logger = LoggerFactory.getLogger(Reportes_Controller.class);
    @Autowired
    private IReportesFinancieros reportesService;
    @Autowired
    private  ReportesImplService reporteService;
    @Autowired
    private ReporteFinancieroService reporteFinancieroService;


   //---------------------------------

    @GetMapping("/saldo-categorias")
    public ResponseEntity<List<SaldoPresupuestoDTO>> getSaldoPorCategoria() {
        return ResponseEntity.ok(reporteService.getSaldoPorCategoria());
    }

/*-------------------Ultimos reportes mejorados y completos--------------------------*/

    @GetMapping("/completo")
    public ResponseEntity<MensajeResponse> getReporteFinancieroCompleto2() {
        try {
            ReporteResumenDTO reporte = reporteFinancieroService.generarReporteCompleto();

            return ResponseEntity.ok()
                    .body(MensajeResponse.builder()
                            .mensaje("Reporte financiero completo generado exitosamente")
                            .object(reporte)
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MensajeResponse.builder()
                            .mensaje("Error al generar el reporte: " + e.getMessage())
                            .object(null)
                            .build());
        }
    }

    //Metodo que retorna un DTO
    @GetMapping("/completo2")
    public ResponseEntity<ReporteResumenDTO> getReporteFinancieroCompleto() {
        try {
            ReporteResumenDTO reporte = reporteFinancieroService.generarReporteCompleto();
            return ResponseEntity.ok(reporte);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    /*------------------------2-----------------------------------------------*/
// Metodo para reportes pasando el año y el mes
    @GetMapping("/mensual/{anio}/{mes}")
    public ResponseEntity<ReporteMensualDTO> getReporteMensual(
            @PathVariable int anio, @PathVariable int mes) {
        try {
            // Validar parámetros
            if (anio < 2000 || anio > 2100) {
                return ResponseEntity.badRequest().build();
            }

            if (mes < 1 || mes > 12) {
                return ResponseEntity.badRequest().build();
            }

            // Generar reporte
            ReporteMensualDTO reporte = reporteFinancieroService.generarReporteMensual(anio, mes);

            // Verificar si hay datos
            if (reporte.getIngresos().isEmpty() &&
                    reporte.getGastos().isEmpty() &&
                    reporte.getPresupuestos().isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Respuesta OK con el DTO
            return ResponseEntity.ok(reporte);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /*--------------------------3------------------------------*/


    //Metodo obsoletos pendientes
      /*@GetMapping("/ingresos-por-categoria")
    public ResponseEntity<?> getIngresosPorCategoria() {
        return ResponseEntity.ok(reportesService.getIngresosPorCategoria());
    }

    @GetMapping("/gastos-por-categoria")
    public ResponseEntity<?> getGastosPorCategoria() {
        return ResponseEntity.ok(reportesService.getGastosPorCategoria());
    }

    @GetMapping("/saldo-acumulado")
    public ResponseEntity<?> getSaldoAcumulado() {
        return ResponseEntity.ok(reportesService.getSaldoAcumulado());
    }

    @GetMapping("/balance-mensual")
    public ResponseEntity<?> getBalanceMensual() {
        return ResponseEntity.ok(reportesService.getBalanceMensual());
    }
*/


}
