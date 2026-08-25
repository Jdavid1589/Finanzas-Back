package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmpleadoRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombres,

        @NotBlank(message = "El numero de documento es obligatorio")
        String noDocumento,

        @Email(message = "El correo no tiene un formato valido")
        String correo,

        String telefonos,

        Boolean enable
) {}
