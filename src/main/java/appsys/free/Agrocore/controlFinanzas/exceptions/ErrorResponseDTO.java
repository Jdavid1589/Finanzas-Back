package appsys.free.Agrocore.controlFinanzas.exceptions;


import java.time.LocalDateTime;
import java.util.List;

/**
 * Forma unica que va a tener CUALQUIER error que devuelva la API,
 * sin importar si es un 404, un 400 de validacion o un 500 inesperado.
 * <p>
 * El objetivo es que el frontend nunca tenga que adivinar el formato del
 * error segun el codigo HTTP: siempre recibe esta misma estructura.
 *
 * @param timestamp momento exacto en el que ocurrio el error
 * @param status    codigo HTTP numerico (404, 400, 500...)
 * @param error     nombre corto del error (ej. "Not Found", "Bad Request")
 * @param mensaje   descripcion legible para mostrar o loguear
 * @param path      endpoint donde ocurrio (ej. "/api/nominas/57")
 * @param errores   detalle campo por campo, SOLO se llena cuando el error
 *                  viene de una validacion de @Valid (en los demas casos
 *                  llega como lista vacia, nunca null, para que el
 *                  frontend no tenga que chequear null antes de iterar)
 */
public record ErrorResponseDTO(
        LocalDateTime timestamp,
        int status,
        String error,
        String mensaje,
        String path,
        List<CampoErrorDTO> errores
) {

    /**
     * Error de un solo campo dentro de una respuesta de validacion.
     * Ejemplo: campo = "nombre", mensaje = "El nombre es obligatorio".
     */
    public record CampoErrorDTO(String campo, String mensaje) {}
}