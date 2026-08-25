package appsys.free.Agrocore.controlFinanzas.dtos;

import java.math.BigDecimal;

public record NominaDTO(
        Long id,
        Integer anio,
        BigDecimal cantidadTipoGranel,
        Integer tipoGranelId,
        String tipoGranelNombre,
        Integer dias,
        BigDecimal horas,
        Integer semana,
        Integer totalNomina,
        Integer totalNominaDias,
        Integer totalNominaHora,
        Integer totalNominaTipoGranel,
        Integer valorDia,
        Integer valorHora,
        Integer valorSemana,
        Integer valorTipoGranel,
        Integer cosechaId,
        Integer empleadoId,
        Boolean estadoPago,
        Integer minutos
) {}
