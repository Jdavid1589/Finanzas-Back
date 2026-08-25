package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Set;

// A proposito NO tiene password: cambiar la contrasena es una accion
// distinta (otro endpoint, ej. POST /usuarios/{id}/cambiar-password), con
// su propia validacion (password actual + nueva). Mezclarlo aqui abriria
// la puerta a que una edicion normal de perfil pise la contrasena sin
// querer si el campo llega vacio o mal manejado en el frontend.
public record UsuarioUpdateDTO(
        @NotBlank(message = "El nombre de usuario es obligatorio")
        String userName,

        @NotBlank @Email(message = "El correo no tiene un formato valido")
        String email,

        @NotBlank(message = "Los nombres son obligatorios")
        String nombres,

        String apellidos,
        String noDocumento,
        String telefonos,
        Boolean enable,

        @NotEmpty(message = "El usuario debe tener al menos un rol")
        Set<Integer> rolesIds
) {}
