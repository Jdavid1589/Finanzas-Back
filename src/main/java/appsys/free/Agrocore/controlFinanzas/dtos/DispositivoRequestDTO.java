package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotNull;

public record DispositivoRequestDTO(
        @NotNull(message = "El usuario asociado al dispositivo es obligatorio")
        Integer usuarioId
) {}
