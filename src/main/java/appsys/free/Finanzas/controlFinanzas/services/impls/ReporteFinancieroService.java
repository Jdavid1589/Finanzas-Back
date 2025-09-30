package appsys.free.Finanzas.controlFinanzas.services.impls;


import appsys.free.Finanzas.controlFinanzas.dtos.*;
import appsys.free.Finanzas.controlFinanzas.repositories.IGastosRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IIngresoRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IPresupuestoRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IReportesFinancieros;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ReporteFinancieroService implements IReportesFinancieros {


    @Autowired
    private IIngresoRepo ingresoRepo;

    @Autowired
    private IGastosRepo gastoRepo;

    @Autowired
    private IPresupuestoRepo presupuestoRepo;

    // 🔹 1. Metodo para Lista de balance mensual Totales
    @Override
    public List<ReporteIngresoDTO> getBalanceMensual_Dto() {
        List<Object[]> ingresosPorCategoria = ingresoRepo.getTotalIngresosPorMesYCategoria();

        // Map para almacenar totales por mes
        Map<String, BigDecimal> totalesPorMes = new HashMap<>();
        BigDecimal totalGeneral = BigDecimal.ZERO;

        // Primera pasada: calcular totales por mes
        for (Object[] row : ingresosPorCategoria) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            String periodo = anio + "-" + String.format("%02d", mes);

            BigDecimal ingreso;
            if (row[3] instanceof BigDecimal) {
                ingreso = (BigDecimal) row[3];
            } else {
                ingreso = BigDecimal.valueOf(((Number) row[3]).doubleValue());
            }

            // Acumular total por mes
            totalesPorMes.put(periodo, totalesPorMes.getOrDefault(periodo, BigDecimal.ZERO).add(ingreso));
            totalGeneral = totalGeneral.add(ingreso);
        }

        // Segunda pasada: crear DTOs con totales
        List<ReporteIngresoDTO> resultado = new ArrayList<>();

        for (Object[] row : ingresosPorCategoria) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            String categoria = (String) row[2];

            BigDecimal ingreso;
            if (row[3] instanceof BigDecimal) {
                ingreso = (BigDecimal) row[3];
            } else {
                ingreso = BigDecimal.valueOf(((Number) row[3]).doubleValue());
            }

            String periodo = anio + "-" + String.format("%02d", mes);
            BigDecimal totalMes = totalesPorMes.get(periodo);

            ReporteIngresoDTO dto = ReporteIngresoDTO.builder()
                    .periodo(periodo)
                    .categoria(categoria)
                    .ingreso(ingreso)
                    .totalMes(totalMes)
                    .totalGeneral(totalGeneral)
                    .build();

            resultado.add(dto);
        }

        return resultado;
    }
    /*-------------------------------------------*/

    // 🔹 2. Metodo para Lista de balance mensual Totales
    public ReporteResumenDTO generarReporteCompleto() {
        // Obtener datos de todas las fuentes
        List<Object[]> ingresosData = ingresoRepo.getIngresosPorMesYCategoria();
        List<Object[]> gastosData = gastoRepo.getGastosPorMesYCategoria();
        List<Object[]> presupuestoData = presupuestoRepo.getPresupuestoPorMesYCategoria();

        // Crear mapas para organizar los datos por período
        Map<String, Map<String, BigDecimal>> ingresosPorPeriodo = new TreeMap<>(Collections.reverseOrder());
        Map<String, Map<String, BigDecimal>> gastosPorPeriodo = new TreeMap<>(Collections.reverseOrder());
        Map<String, Map<String, BigDecimal>> presupuestoPorPeriodo = new TreeMap<>(Collections.reverseOrder());

        // Procesar ingresos
        procesarDatos(ingresosData, ingresosPorPeriodo, "INGRESO");

        // Procesar gastos
        procesarDatos(gastosData, gastosPorPeriodo, "GASTO");

        // Procesar presupuesto
        procesarDatos(presupuestoData, presupuestoPorPeriodo, "PRESUPUESTO");

        // Obtener todos los períodos únicos
        Set<String> periodos = new TreeSet<>(Collections.reverseOrder());
        periodos.addAll(ingresosPorPeriodo.keySet());
        periodos.addAll(gastosPorPeriodo.keySet());
        periodos.addAll(presupuestoPorPeriodo.keySet());

        // Crear el reporte detallado
        List<ReporteFinancieroMensualDTO> reporteMensual = new ArrayList<>();
        BigDecimal totalGeneralIngresos = BigDecimal.ZERO;
        BigDecimal totalGeneralPresupuesto = BigDecimal.ZERO;
        BigDecimal totalGeneralGastos = BigDecimal.ZERO;

        for (String periodo : periodos) {
            ReporteFinancieroMensualDTO reporteMes = generarReporteMensual(
                    periodo,
                    ingresosPorPeriodo.get(periodo),
                    gastosPorPeriodo.get(periodo),
                    presupuestoPorPeriodo.get(periodo)
            );

            reporteMensual.add(reporteMes);

            // Acumular totales generales
            totalGeneralIngresos = totalGeneralIngresos.add(reporteMes.getTotalIngresos());
            totalGeneralPresupuesto = totalGeneralPresupuesto.add(reporteMes.getTotalPresupuesto());
            totalGeneralGastos = totalGeneralGastos.add(reporteMes.getTotalGastos());
        }

        // Crear resumen general
        return ReporteResumenDTO.builder()
                .totalGeneralIngresos(totalGeneralIngresos)
                .totalGeneralPresupuesto(totalGeneralPresupuesto)
                .totalGeneralGastos(totalGeneralGastos)
                .balanceGeneral(totalGeneralIngresos.subtract(totalGeneralGastos))
                .detalleMensual(reporteMensual)
                .build();
    }

    /*-------------------------------------------*/
    public ReporteMensualDTO generarReporteMensual(int anio, int mes) {
        // Obtener datos del mes específico
        List<Object[]> ingresosData = ingresoRepo.getIngresosPorCategoria(anio, mes);
        List<Object[]> gastosData = gastoRepo.getGastosPorCategoria(anio, mes);
        List<Object[]> presupuestoData = presupuestoRepo.getPresupuestoPorCategoria(anio, mes);

        // Convertir a DTOs
        List<CategoriaDetalleDTO> ingresos = convertirADTO(ingresosData, "INGRESO");
        List<CategoriaDetalleDTO> gastos = convertirADTO(gastosData, "GASTO");
        List<CategoriaDetalleDTO> presupuestos = convertirADTO(presupuestoData, "PRESUPUESTO");

        // Calcular totales
        BigDecimal totalIngresos = calcularTotal(ingresos);
        BigDecimal totalGastos = calcularTotal(gastos);
        BigDecimal totalPresupuestado = calcularTotal(presupuestos);

        // Asignar presupuesto a cada gasto
        Map<String, BigDecimal> presupuestoMap = new HashMap<>();
        for (CategoriaDetalleDTO presupuesto : presupuestos) {
            presupuestoMap.put(presupuesto.getCategoria(), presupuesto.getMonto());
        }

        for (CategoriaDetalleDTO gasto : gastos) {
            BigDecimal presupuesto = presupuestoMap.getOrDefault(gasto.getCategoria(), BigDecimal.ZERO);
            gasto.setPresupuestado(presupuesto);
            gasto.setDiferencia(presupuesto.subtract(gasto.getMonto()));
        }

        String periodo = anio + "-" + String.format("%02d", mes);

        return ReporteMensualDTO.builder()
                .periodo(periodo)
                .ingresos(ingresos)
                .gastos(gastos)
                .presupuestos(presupuestos)
                .totalIngresos(totalIngresos)
                .totalGastos(totalGastos)
                .totalPresupuestado(totalPresupuestado)
                .balance(totalIngresos.subtract(totalGastos))
                .desviacionPresupuesto(totalPresupuestado.subtract(totalGastos))
                .build();
    }

    private List<CategoriaDetalleDTO> convertirADTO(List<Object[]> data, String tipo) {
        List<CategoriaDetalleDTO> result = new ArrayList<>();
        for (Object[] row : data) {
            String categoria = (String) row[0];
            BigDecimal monto = convertToBigDecimal2(row[1]);

            result.add(CategoriaDetalleDTO.builder()
                    .categoria(categoria)
                    .monto(monto)
                    .build());
        }
        return result;
    }


    /*----------------Metodo auxliares para el metodo generarReporteMensual--------------------*/

    private BigDecimal calcularTotal(List<CategoriaDetalleDTO> items) {
        return items.stream()
                .map(CategoriaDetalleDTO::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /*------------------------------------*/
    private BigDecimal convertToBigDecimal2(Object value) {
        if (value instanceof BigDecimal) return (BigDecimal) value;
        if (value instanceof Double) return BigDecimal.valueOf((Double) value);
        if (value instanceof Number) return BigDecimal.valueOf(((Number) value).doubleValue());
        return BigDecimal.ZERO;
    }

    /*---------------------------------------- --------------------*/
    private void procesarDatos(List<Object[]> data, Map<String, Map<String, BigDecimal>> mapa, String tipo) {
        for (Object[] row : data) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            String categoria = (String) row[2];
            BigDecimal monto = convertToBigDecimal(row[3]);

            String periodo = anio + "-" + String.format("%02d", mes);

            mapa.computeIfAbsent(periodo, k -> new HashMap<>())
                    .put(tipo + "_" + categoria, monto);
        }
    }

    private ReporteFinancieroMensualDTO generarReporteMensual  (String periodo,  Map<String, BigDecimal> ingresosMap,
             Map<String, BigDecimal> gastosMap, Map<String, BigDecimal> presupuestoMap) {

        List<CategoriaDetalleDTO> categorias = new ArrayList<>();
        BigDecimal totalIngresos = BigDecimal.ZERO;
        BigDecimal totalGastos = BigDecimal.ZERO;
        BigDecimal totalPresupuesto = BigDecimal.ZERO;

        // Procesar ingresos
        if (ingresosMap != null) {
            for (Map.Entry<String, BigDecimal> entry : ingresosMap.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("INGRESO_")) {
                    String categoria = key.substring(8);
                    BigDecimal monto = entry.getValue();

                    categorias.add(CategoriaDetalleDTO.builder()
                            .tipo("INGRESO")
                            .categoria(categoria)
                            .monto(monto)
                            .build());

                    totalIngresos = totalIngresos.add(monto);
                }
            }
        }

        // Procesar gastos y presupuesto
        if (gastosMap != null) {
            for (Map.Entry<String, BigDecimal> entry : gastosMap.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("GASTO_")) {
                    String categoria = key.substring(6);
                    BigDecimal montoGasto = entry.getValue();
                    BigDecimal montoPresupuesto = obtenerPresupuesto(presupuestoMap, categoria);

                    categorias.add(CategoriaDetalleDTO.builder()
                            .tipo("GASTO")
                            .categoria(categoria)
                            .monto(montoGasto)
                            .presupuestado(montoPresupuesto)
                            .diferencia(montoPresupuesto.subtract(montoGasto))
                            .build());

                    totalGastos = totalGastos.add(montoGasto);
                    totalPresupuesto = totalPresupuesto.add(montoPresupuesto);
                }
            }
        }

        // Procesar presupuesto sin gastos
        if (presupuestoMap != null) {
            for (Map.Entry<String, BigDecimal> entry : presupuestoMap.entrySet()) {
                String key = entry.getKey();
                if (key.startsWith("PRESUPUESTO_")) {
                    String categoria = key.substring(12);

                    // Verificar si ya se procesó como gasto
                    boolean existeGasto = categorias.stream()
                            .anyMatch(c -> "GASTO".equals(c.getTipo()) && categoria.equals(c.getCategoria()));

                    if (!existeGasto) {
                        BigDecimal montoPresupuesto = entry.getValue();

                        categorias.add(CategoriaDetalleDTO.builder()
                                .tipo("PRESUPUESTO")
                                .categoria(categoria)
                                .presupuestado(montoPresupuesto)
                                .monto(BigDecimal.ZERO)
                                .diferencia(montoPresupuesto)
                                .build());

                        totalPresupuesto = totalPresupuesto.add(montoPresupuesto);
                    }
                }
            }
        }

        // Ordenar categorías
        categorias.sort(Comparator.comparing(CategoriaDetalleDTO::getTipo)
                .thenComparing(CategoriaDetalleDTO::getCategoria));

        return ReporteFinancieroMensualDTO.builder()
                .periodo(periodo)
                .categorias(categorias)
                .totalIngresos(totalIngresos)
                .totalGastos(totalGastos)
                .totalPresupuesto(totalPresupuesto)
                .balance(totalIngresos.subtract(totalGastos))
                .desviacionPresupuesto(totalPresupuesto.subtract(totalGastos))
                .build();
    }

    private BigDecimal obtenerPresupuesto(Map<String, BigDecimal> presupuestoMap, String categoria) {
        if (presupuestoMap != null) {
            return presupuestoMap.getOrDefault("PRESUPUESTO_" + categoria, BigDecimal.ZERO);
        }
        return BigDecimal.ZERO;
    }

    private BigDecimal convertToBigDecimal(Object value) {
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        } else if (value instanceof Double) {
            return BigDecimal.valueOf((Double) value);
        } else if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        return BigDecimal.ZERO;
    }





/*-------------------------------Pendientes--------------------------------------------*/


    /*

     */
    /*Reporte Ingresos*//*

    @Override
    public List<Object[]> getIngresosPorCategoria() {
        return ingresoRepo.getIngresosPorCategoria();
    }

    */
    /*Reporte Gastos*//*

    @Override
    public List<Object[]> getGastosPorCategoria() {
        return gastoRepo.getGastosPorCategoria();
    }


    */
    /*Reporte Generales*//*

    @Override
    public List<Object[]> getSaldoAcumulado() {
        List<Object[]> ingresos = ingresoRepo.getTotalIngresosPorMes();
        List<Object[]> gastos = gastoRepo.getTotalGastosPorMes();

        Map<String, BigDecimal> balances = new TreeMap<>(); // clave = yyyy-MM

        // Ingresos
        for (Object[] row : ingresos) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            BigDecimal total = BigDecimal.valueOf(((Number) row[2]).doubleValue());

            String key = anio + "-" + String.format("%02d", mes);
            balances.put(key, balances.getOrDefault(key, BigDecimal.ZERO).add(total));
        }

        // Gastos
        for (Object[] row : gastos) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            BigDecimal total = BigDecimal.valueOf(((Number) row[2]).doubleValue());

            String key = anio + "-" + String.format("%02d", mes);
            balances.put(key, balances.getOrDefault(key, BigDecimal.ZERO).subtract(total));
        }

        // Resultado acumulado
        List<Object[]> resultado = new ArrayList<>();
        BigDecimal acumulado = BigDecimal.ZERO;
        for (Map.Entry<String, BigDecimal> entry : balances.entrySet()) {
            acumulado = acumulado.add(entry.getValue());
            resultado.add(new Object[]{entry.getKey(), acumulado});
        }

        return resultado;
    }

    @Override
    public List<Map<String, Object>> getBalanceMensual() {
        List<Object[]> ingresos = ingresoRepo.getTotalIngresosPorMes();
        List<Object[]> gastos = gastoRepo.getTotalGastosPorMes();

        Map<String, BigDecimal> mapaIngresos = new HashMap<>();
        Map<String, BigDecimal> mapaGastos = new HashMap<>();

        for (Object[] row : ingresos) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            BigDecimal total = BigDecimal.valueOf(((Number) row[2]).doubleValue());
            String key = anio + "-" + String.format("%02d", mes);
            mapaIngresos.put(key, total);
        }

        for (Object[] row : gastos) {
            int anio = ((Number) row[0]).intValue();
            int mes = ((Number) row[1]).intValue();
            BigDecimal total = BigDecimal.valueOf(((Number) row[2]).doubleValue());
            String key = anio + "-" + String.format("%02d", mes);
            mapaGastos.put(key, total);
        }

        // Unificar claves
        Set<String> meses = new TreeSet<>();
        meses.addAll(mapaIngresos.keySet());
        meses.addAll(mapaGastos.keySet());

        List<Map<String, Object>> resultado = new ArrayList<>();
        for (String periodo : meses) {
            BigDecimal ingresosMes = mapaIngresos.getOrDefault(periodo, BigDecimal.ZERO);
            BigDecimal gastosMes = mapaGastos.getOrDefault(periodo, BigDecimal.ZERO);
            BigDecimal balance = ingresosMes.subtract(gastosMes);

            Map<String, Object> fila = new HashMap<>();
            fila.put("periodo", periodo);
            fila.put("ingresos", ingresosMes);
            fila.put("gastos", gastosMes);
            fila.put("balance", balance);

            resultado.add(fila);
        }

        return resultado;
    }

*/









}
