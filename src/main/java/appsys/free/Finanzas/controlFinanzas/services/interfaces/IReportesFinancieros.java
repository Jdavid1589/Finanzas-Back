package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.ReporteIngresoDTO;
import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IReportesFinancieros {

    List<ReporteIngresoDTO> getBalanceMensual_Dto();

  //  List<SaldoPresupuestoDTO> getSaldoPresupuestoVsGastos();


 /* List<Object[]> getIngresosPorCategoria();

    List<Object[]> getGastosPorCategoria();

    List<Object[]> getSaldoAcumulado();

    List<Map<String, Object>> getBalanceMensual();
*/




}
