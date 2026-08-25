package appsys.free.Agrocore.controlFinanzas.dtos;

import appsys.free.Agrocore.controlFinanzas.entities.EstadoCosecha;
import jakarta.validation.constraints.NotNull;

public record CambiarEstadoCosechaRequestDTO(
        @NotNull(message = "El nuevo estado es obligatorio")
        EstadoCosecha estado
) {}
