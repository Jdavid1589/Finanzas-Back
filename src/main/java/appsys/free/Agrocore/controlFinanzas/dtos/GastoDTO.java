package appsys.free.Agrocore.controlFinanzas.dtos;

import java.time.LocalDate;

public record GastoDTO(
        Long id,
        Integer cantidad,
        Integer costoInsumo,
        String detalle,
        LocalDate fecha,
        String tipoFlete,
        String tipoServicio,
        Integer total,
        Integer cosechaId,
        Integer insumoId,
        Integer tipoGastoId
) {}
