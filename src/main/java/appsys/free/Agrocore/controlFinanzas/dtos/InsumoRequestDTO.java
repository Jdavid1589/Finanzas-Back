package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record InsumoRequestDTO(
        @NotBlank(message = "El nombre del insumo es obligatorio")
        String nombre,

        @NotNull @PositiveOrZero(message = "La cantidad no puede ser negativa")
        Integer cantidad,

        @NotNull @PositiveOrZero(message = "El costo no puede ser negativo")
        Integer costo
) {}
