package appsys.free.Agrocore.controlFinanzas.entities;

/**
 * Estados posibles del ciclo de vida de una Cosecha.
 * <p>
 * Transiciones permitidas (validadas en CosechaServiceImpl.cambiarEstado()):
 * ACTIVA → LIQUIDADA
 * ACTIVA → CANCELADA
 * LIQUIDADA y CANCELADA son estados finales -- no se puede salir de ellos.
 */
public enum EstadoCosecha {
    ACTIVA,
    LIQUIDADA,
    CANCELADA
}
