package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// Se usa tanto para crear como para editar un cliente: no hay ninguna regla
// que difiera entre las dos operaciones, asi que un solo DTO es suficiente.
public record ClienteRequestDTO(
        @NotBlank(message = "El nombre es obligatorio")
        String nombre,

        @NotBlank(message = "El NIT es obligatorio")
        String nit,

        @Email(message = "El correo no tiene un formato valido")
        String correo,

        String telefonos
) {}
