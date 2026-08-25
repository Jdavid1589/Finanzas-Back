package appsys.free.Agrocore.controlFinanzas.exceptions;

import appsys.free.Agrocore.controlFinanzas.exceptions.ErrorResponseDTO.CampoErrorDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Punto UNICO donde se atrapan las excepciones que se escapan de cualquier
 * controller de la aplicacion y se convierten en un {@link ErrorResponseDTO}
 * con formato consistente.
 * <p>
 * {@code @RestControllerAdvice} es lo que hace que esta clase "vea" las
 * excepciones de TODOS los {@code @RestController} del proyecto sin que
 * cada uno tenga que hacer try/catch manualmente. Es la version REST de
 * {@code @ControllerAdvice} (ya trae el {@code @ResponseBody} incluido).
 * <p>
 * Orden de prioridad: Spring elige el {@code @ExceptionHandler} mas
 * especifico que calce con la excepcion lanzada. Por eso {@code Exception}
 * (el mas generico) va al final, como red de seguridad.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Atrapa los {@link ResponseStatusException} que lanzamos a mano en
     * cada {@code buscarOFallar(id)} de los services (ej. "Cliente no
     * encontrado con id 7"). Reusa el status HTTP que ya viene definido
     * en la excepcion (404, 409, etc.) en vez de asumir uno fijo.
     *
     * @param ex      la excepcion capturada, con su status y mensaje
     * @param request usado solo para saber en que endpoint ocurrio
     * @return el error en formato {@link ErrorResponseDTO}, con el mismo
     *         codigo HTTP que traia la excepcion original
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponseDTO> handleResponseStatusException(
            ResponseStatusException ex,
            HttpServletRequest request
    ) {
        HttpStatusCode status = ex.getStatusCode();

        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                status.value(),
                HttpStatus.valueOf(status.value()).getReasonPhrase(),
                ex.getReason(),
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(status).body(body);
    }

    /**
     * Atrapa los fallos de validacion de Bean Validation (@NotNull,
     * @NotBlank, @Positive, etc.) que saltan cuando un {@code @Valid
     * @RequestBody} no cumple las reglas del DTO de request.
     * <p>
     * A diferencia de los demas handlers, aqui SI se llena la lista
     * {@code errores} del {@link ErrorResponseDTO}: el frontend necesita
     * saber exactamente que campo fallo para marcarlo en el formulario,
     * no solo un mensaje generico de "datos invalidos".
     *
     * @param ex      excepcion que trae, por cada campo invalido, el
     *                nombre del campo y el mensaje configurado en la
     *                anotacion (ej. @NotBlank(message = "..."))
     * @param request usado solo para saber en que endpoint ocurrio
     * @return 400 Bad Request con el detalle de cada campo que fallo
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        List<CampoErrorDTO> errores = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new CampoErrorDTO(
                        error.getField(),
                        error.getDefaultMessage()
                ))
                .toList();

        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.getReasonPhrase(),
                "Uno o mas campos no son validos",
                request.getRequestURI(),
                errores
        );

        return ResponseEntity.badRequest().body(body);
    }

    /**
     * Atrapa los bloqueos de {@code @PreAuthorize}/{@code @Secured}: cuando
     * un usuario autenticado SI tiene un token valido pero NO tiene el rol
     * requerido para esa operacion (ej. ROLE_USER intentando un DELETE
     * restringido a ROLE_ADMIN, o un usuario sin ningun rol asignado).
     * <p>
     * Sin este handler, {@link AccessDeniedException} caia en el handler
     * generico de abajo y salia como 500 -- lo cual es incorrecto: un
     * permiso insuficiente es un 403 (Forbidden), no un error del servidor.
     *
     * @param ex      la excepcion lanzada por el interceptor de seguridad
     * @param request usado solo para saber en que endpoint ocurrio
     * @return 403 Forbidden con mensaje claro de falta de permisos
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAccessDeniedException(
            AccessDeniedException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                HttpStatus.FORBIDDEN.getReasonPhrase(),
                "No tienes permisos suficientes para realizar esta accion",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(body);
    }

    /**
     * Red de seguridad para violaciones de integridad referencial (FK) que
     * lleguen crudas desde la BD -- por ejemplo, si algun dia se agrega un
     * endpoint nuevo que borra un registro sin la validacion explicita que
     * ya tiene {@code CosechaServiceImpl.delete()} para Gasto/Nomina/Venta.
     * <p>
     * En el flujo normal de hoy, esto NO deberia dispararse (la validacion
     * explicita bloquea antes de siquiera intentar el DELETE) -- esto es
     * una segunda capa de defensa, no la primera.
     *
     * @param ex      la excepcion de integridad de datos
     * @param request usado solo para saber en que endpoint ocurrio
     * @return 409 Conflict con mensaje generico (el detalle real de la FK
     *         queda en el log del servidor, no se expone al cliente)
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDTO> handleDataIntegrityViolationException(
            DataIntegrityViolationException ex,
            HttpServletRequest request
    ) {
        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                HttpStatus.CONFLICT.getReasonPhrase(),
                "No se puede completar la operacion porque el registro tiene datos asociados",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    /**
     * Red de seguridad final: cualquier excepcion NO prevista por los
     * handlers anteriores (un NullPointerException, un error de
     * conexion a la BD, etc.) cae aqui en vez de mostrarle al cliente
     * el stacktrace crudo de Spring (que expone detalles internos y se
     * ve poco profesional en una respuesta de API).
     * <p>
     * Siempre responde 500: si esto se activa seguido en producción,
     * es señal de que falta un {@code @ExceptionHandler} mas especifico
     * para ese caso, no de que este metodo este fallando.
     *
     * @param ex      la excepcion inesperada
     * @param request usado solo para saber en que endpoint ocurrio
     * @return 500 Internal Server Error con mensaje generico (nunca se
     *         expone {@code ex.getMessage()} crudo al cliente por
     *         seguridad; el detalle real deberia ir a un logger, no al
     *         response -- ver nota mas abajo)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGenericException(
            Exception ex,
            HttpServletRequest request
    ) {
        // TODO: aqui es donde deberias loguear ex (log.error("...", ex))
        // con un logger real (SLF4J) para poder investigar el error en
        // tus logs de servidor -- el cliente nunca ve ese detalle, pero
        // tu si lo necesitas para depurar.

        ErrorResponseDTO body = new ErrorResponseDTO(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocurrio un error inesperado. Intenta de nuevo mas tarde.",
                request.getRequestURI(),
                List.of()
        );

        return ResponseEntity.internalServerError().body(body);
    }
}