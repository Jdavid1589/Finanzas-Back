package appsys.free.Agrocore.controlFinanzas.dtos;

public record ClienteDTO(
        Integer id,
        String correo,
        String nit,
        String nombre,
        String telefonos
) {}
