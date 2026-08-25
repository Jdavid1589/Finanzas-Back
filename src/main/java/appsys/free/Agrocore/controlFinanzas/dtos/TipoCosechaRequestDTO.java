package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotBlank;

public record TipoCosechaRequestDTO(
        @NotBlank(message = "El tipo de cosecha es obligatorio")
        String tipo
) {}
