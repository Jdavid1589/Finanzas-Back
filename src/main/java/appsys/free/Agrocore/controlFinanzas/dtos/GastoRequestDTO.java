package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

public record GastoRequestDTO(
        @NotNull(message = "La cosecha es obligatoria")
        Integer cosechaId,

        @NotNull(message = "El tipo de gasto es obligatorio")
        Integer tipoGastoId,

        Integer insumoId,

        @NotNull @Positive(message = "La cantidad debe ser mayor a cero")
        Integer cantidad,

        @NotNull @PositiveOrZero(message = "El costo del insumo no puede ser negativo")
        Integer costoInsumo,

        String detalle,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        String tipoFlete,

        String tipoServicio
) {}
