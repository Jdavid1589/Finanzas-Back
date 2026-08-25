package appsys.free.Agrocore.controlFinanzas.dtos;

public record EmpleadoDTO(
        Integer id,
        String correo,
        Boolean enable,
        String noDocumento,
        String nombres,
        String telefonos
) {}
