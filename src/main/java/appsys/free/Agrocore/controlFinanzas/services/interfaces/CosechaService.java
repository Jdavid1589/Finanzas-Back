package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.CosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.entities.EstadoCosecha;

import java.util.List;

public interface CosechaService {

    List<CosechaDTO> findAll();

    CosechaDTO findById(Integer id);

    CosechaDTO create(CosechaRequestDTO dto);

    CosechaDTO update(Integer id, CosechaRequestDTO dto);

    void delete(Integer id);

    /**
     * Vuelve a calcular, desde cero, todos los campos "total_*" de una
     * cosecha (totalInsumos, totalValorFletes, totalServicios, totalOtros,
     * totalValorNomina, totalVenta, utilidad) sumando sus gastos, nomina y
     * ventas asociados. Se llama automaticamente cada vez que se crea,
     * edita o elimina un Gasto, Nomina o Venta de esa cosecha -- nunca
     * deberias necesitar llamarlo manualmente desde un controller.
     *
     * @param cosechaId id de la cosecha a recalcular
     */
    void recalcularTotales(Integer cosechaId);

    /**
     * Cambia el estado de una cosecha, validando que la transicion sea
     * permitida (ver reglas en {@link appsys.free.Agrocore.controlFinanzas.entities.EstadoCosecha}).
     *
     * @param cosechaId  id de la cosecha
     * @param nuevoEstado estado destino
     * @throws org.springframework.web.server.ResponseStatusException 409 si la transicion no es valida
     */
    CosechaDTO cambiarEstado(Integer cosechaId, EstadoCosecha nuevoEstado);

    CosechaDTO reabrir(Integer cosechaId);
}
