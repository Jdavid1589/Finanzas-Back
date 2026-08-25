package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import java.time.LocalDate;

// valor_total NO viene en el request: el service lo calcula como
// cantidad * valorUnidad, para que nunca quede inconsistente.
public record VentaRequestDTO(
        @NotNull(message = "La cosecha es obligatoria")
        Integer cosechaId,

        @NotNull(message = "El cliente es obligatorio")
        Integer clienteId,

        @NotNull @Positive(message = "La cantidad debe ser mayor a cero")
        Integer cantidad,

        @NotNull @PositiveOrZero(message = "El valor unitario no puede ser negativo")
        Integer valorUnidad,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha
) {}
