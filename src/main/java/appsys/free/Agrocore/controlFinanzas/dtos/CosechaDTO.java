package appsys.free.Agrocore.controlFinanzas.dtos;

import appsys.free.Agrocore.controlFinanzas.entities.EstadoCosecha;

import java.time.LocalDate;

// Las relaciones se exponen "aplanadas" (id + nombre del catalogo), nunca la
// entidad completa: evita el problema de serializacion ciclica de las
// relaciones bidireccionales (Cosecha <-> Gasto/Nomina/Venta).
//
// "estado" ahora es el enum EstadoCosecha -- Jackson lo serializa/deserializa
// como el string de su nombre ("ACTIVA", "LIQUIDADA", "CANCELADA") de forma
// automatica, sin configuracion adicional.
public record CosechaDTO(
        Integer id,
        String nombre,
        Integer tipoCosechaId,
        String tipoCosechaNombre,
        Integer tipoGranelId,
        String tipoGranelNombre,
        LocalDate fechaInicio,
        LocalDate fechaFinal,
        Integer totalInsumos,
        Integer totalValorFletes,
        Integer totalValorNomina,
        Integer totalVenta,
        Integer utilidad,
        EstadoCosecha estado,
        Integer totalOtros,
        Double totalProduccion,
        Integer totalServicios
) {}
