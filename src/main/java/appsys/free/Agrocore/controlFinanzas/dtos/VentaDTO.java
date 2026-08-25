package appsys.free.Agrocore.controlFinanzas.dtos;

import java.time.LocalDate;

public record VentaDTO(
        Long id,
        Integer cantidad,
        LocalDate fecha,
        Integer valorTotal,
        Integer valorUnidad,
        Integer cosechaId,
        Integer clienteId
) {}
