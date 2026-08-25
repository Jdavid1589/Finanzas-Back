package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotBlank;

public record TipoGranelRequestDTO(
        @NotBlank(message = "El tipo de granel es obligatorio")
        String tipo
) {}
