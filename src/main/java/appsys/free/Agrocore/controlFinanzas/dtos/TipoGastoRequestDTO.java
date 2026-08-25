package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotBlank;

public record TipoGastoRequestDTO(
        @NotBlank(message = "El tipo de gasto es obligatorio")
        String tipo
) {}
