package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Cosecha;
import appsys.free.Agrocore.controlFinanzas.entities.EstadoCosecha;
import appsys.free.Agrocore.controlFinanzas.entities.Gasto;
import appsys.free.Agrocore.controlFinanzas.entities.Nomina;
import appsys.free.Agrocore.controlFinanzas.entities.TipoCosecha;
import appsys.free.Agrocore.controlFinanzas.entities.TipoGranel;
import appsys.free.Agrocore.controlFinanzas.entities.Venta;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.CosechaMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.CosechaRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.GastoRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.NominaRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.TipoCosechaRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.TipoGranelRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.VentaRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.CosechaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CosechaServiceImpl implements CosechaService {

    // Reglas de transicion: desde cada estado, a que estados SI se puede
    // pasar. ACTIVA es el unico estado "vivo" -- LIQUIDADA y CANCELADA son
    // finales, no se puede salir de ellos. Centralizado aqui para que
    // cambiar la regla de negocio sea editar un solo lugar, no rastrear
    // "if" repartidos por el service.
    private static final Map<EstadoCosecha, Set<EstadoCosecha>> TRANSICIONES_VALIDAS = new EnumMap<>(Map.of(
            EstadoCosecha.ACTIVA, EnumSet.of(EstadoCosecha.LIQUIDADA, EstadoCosecha.CANCELADA),
            EstadoCosecha.LIQUIDADA, EnumSet.noneOf(EstadoCosecha.class),
            EstadoCosecha.CANCELADA, EnumSet.noneOf(EstadoCosecha.class)
    ));

    private final CosechaRepository cosechaRepository;
    private final TipoCosechaRepository tipoCosechaRepository;
    private final TipoGranelRepository tipoGranelRepository;
    // Se necesitan aqui SOLO para recalcularTotales() -- Cosecha es quien
    // "sabe" como agregar sus propios gastos/nomina/ventas, aunque esas
    // entidades pertenezcan a otros modulos del dominio.
    private final GastoRepository gastoRepository;
    private final NominaRepository nominaRepository;
    private final VentaRepository ventaRepository;
    private final CosechaMapper cosechaMapper;

    @Override
    @Transactional(readOnly = true) // evita LazyInitializationException al leer tipoCosecha/tipoGranel
    public List<CosechaDTO> findAll() {
        return cosechaMapper.toDtoList(cosechaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public CosechaDTO findById(Integer id) {
        return cosechaMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public CosechaDTO create(CosechaRequestDTO dto) {
        Cosecha cosecha = cosechaMapper.toEntity(dto);
        cosecha.setTipoCosecha(buscarTipoCosecha(dto.tipoCosechaId()));
        cosecha.setTipoGranel(buscarTipoGranel(dto.tipoGranelId()));

        // Una cosecha nueva arranca "en cero" y ACTIVA (estado = 1). Los
        // totales se recalculan solos en cuanto se le registre el primer
        // gasto/nomina/venta -- aqui solo se inicializan.
        cosecha.setTotalInsumos(0);
        cosecha.setTotalValorFletes(0);
        cosecha.setTotalValorNomina(0);
        cosecha.setTotalVenta(0);
        cosecha.setUtilidad(0);
        cosecha.setTotalOtros(0);
        // totalProduccion NO se toca en el recalculo automatico -- no hay
        // regla de negocio confirmada sobre que representa (cantidad
        // cosechada en que unidad, de donde sale). Queda en 0 hasta que
        // definas esa regla.
        cosecha.setTotalProduccion(0.0);
        cosecha.setTotalServicios(0);
        cosecha.setEstado(EstadoCosecha.ACTIVA);

        return cosechaMapper.toDto(cosechaRepository.save(cosecha));
    }

    @Override
    @Transactional
    public CosechaDTO update(Integer id, CosechaRequestDTO dto) {
        Cosecha cosecha = buscarOFallar(id);
        cosecha.setNombre(dto.nombre());
        cosecha.setFechaInicio(dto.fechaInicio());
        cosecha.setFechaFinal(dto.fechaFinal());
        cosecha.setTipoCosecha(buscarTipoCosecha(dto.tipoCosechaId()));
        cosecha.setTipoGranel(buscarTipoGranel(dto.tipoGranelId()));
        // Los totales NO se tocan aqui a proposito: se recalculan solo a
        // traves de recalcularTotales(), disparado por los servicios de
        // Gasto/Nomina/Venta -- nunca directo desde el formulario de cosecha.
        return cosechaMapper.toDto(cosechaRepository.save(cosecha));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        Cosecha cosecha = buscarOFallar(id);

        // Antes del CascadeType.ALL que quitamos, borrar una cosecha borraba
        // en cascada TODOS sus gastos/nomina/ventas sin avisar -- peligroso
        // para datos financieros. Ahora se bloquea explicitamente: para
        // borrar una cosecha, primero hay que borrar (o reasignar) sus
        // registros asociados uno por uno, a proposito, desde sus propios
        // endpoints (/api/gastos/{id}, /api/nominas/{id}, /api/ventas/{id}).
        List<String> bloqueos = new ArrayList<>();

        long cantidadGastos = gastoRepository.findByCosechaId(id).size();
        if (cantidadGastos > 0) {
            bloqueos.add(cantidadGastos + " gasto(s)");
        }

        long cantidadNominas = nominaRepository.findByCosechaId(id).size();
        if (cantidadNominas > 0) {
            bloqueos.add(cantidadNominas + " registro(s) de nomina");
        }

        long cantidadVentas = ventaRepository.findByCosechaId(id).size();
        if (cantidadVentas > 0) {
            bloqueos.add(cantidadVentas + " venta(s)");
        }

        if (!bloqueos.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar la cosecha '" + cosecha.getNombre() + "' porque tiene "
                            + String.join(", ", bloqueos) + " asociados. Elimina o reasigna esos "
                            + "registros primero.");
        }

        cosechaRepository.delete(cosecha);
    }

    @Override
    @Transactional
    public void recalcularTotales(Integer cosechaId) {
        Cosecha cosecha = buscarOFallar(cosechaId);

        int totalInsumos = 0;
        int totalValorFletes = 0;
        int totalServicios = 0;
        int totalOtros = 0;

        List<Gasto> gastos = gastoRepository.findByCosechaId(cosechaId);
        for (Gasto gasto : gastos) {
            int total = gasto.getTotal() != null ? gasto.getTotal() : 0;

            // Regla asumida (confirmar con la regla de negocio real):
            // 1) tiene insumo asociado -> es un gasto de insumo
            // 2) si no, pero trae tipoFlete -> es un flete
            // 3) si no, pero trae tipoServicio -> es un servicio
            // 4) si no cae en ninguno -> otros
            if (gasto.getInsumo() != null) {
                totalInsumos += total;
            } else if (gasto.getTipoFlete() != null && !gasto.getTipoFlete().isBlank()) {
                totalValorFletes += total;
            } else if (gasto.getTipoServicio() != null && !gasto.getTipoServicio().isBlank()) {
                totalServicios += total;
            } else {
                totalOtros += total;
            }
        }

        int totalValorNomina = 0;
        List<Nomina> nominas = nominaRepository.findByCosechaId(cosechaId);
        for (Nomina nomina : nominas) {
            totalValorNomina += nomina.getTotalNomina() != null ? nomina.getTotalNomina() : 0;
        }

        int totalVenta = 0;
        List<Venta> ventas = ventaRepository.findByCosechaId(cosechaId);
        for (Venta venta : ventas) {
            totalVenta += venta.getValorTotal() != null ? venta.getValorTotal() : 0;
        }

        int utilidad = totalVenta - (totalInsumos + totalValorFletes + totalServicios + totalOtros + totalValorNomina);

        cosecha.setTotalInsumos(totalInsumos);
        cosecha.setTotalValorFletes(totalValorFletes);
        cosecha.setTotalServicios(totalServicios);
        cosecha.setTotalOtros(totalOtros);
        cosecha.setTotalValorNomina(totalValorNomina);
        cosecha.setTotalVenta(totalVenta);
        cosecha.setUtilidad(utilidad);

        cosechaRepository.save(cosecha);
    }

    @Override
    @Transactional
    public CosechaDTO cambiarEstado(Integer cosechaId, EstadoCosecha nuevoEstado) {
        Cosecha cosecha = buscarOFallar(cosechaId);
        EstadoCosecha estadoActual = cosecha.getEstado();

        if (estadoActual == nuevoEstado) {
            // Pedir el mismo estado que ya tiene no es un error de negocio,
            // simplemente no hace nada -- evita que el frontend tenga que
            // chequear el estado actual antes de poder llamar este endpoint.
            return cosechaMapper.toDto(cosecha);
        }

        Set<EstadoCosecha> permitidos = TRANSICIONES_VALIDAS.getOrDefault(estadoActual, Set.of());
        if (!permitidos.contains(nuevoEstado)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede cambiar de " + estadoActual + " a " + nuevoEstado);
        }

        cosecha.setEstado(nuevoEstado);
        return cosechaMapper.toDto(cosechaRepository.save(cosecha));
    }

    private Cosecha buscarOFallar(Integer id) {
        return cosechaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cosecha no encontrada con id " + id));
    }

    private TipoCosecha buscarTipoCosecha(Integer id) {
        return tipoCosechaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tipo de cosecha no encontrado con id " + id));
    }

    private TipoGranel buscarTipoGranel(Integer id) {
        return tipoGranelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tipo de granel no encontrado con id " + id));
    }


    @Override
    @Transactional
    public CosechaDTO reabrir(Integer cosechaId) {

        Cosecha cosecha = buscarOFallar(cosechaId);

        if (cosecha.getEstado() != EstadoCosecha.LIQUIDADA) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Solo se puede reabrir una cosecha LIQUIDADA"
            );
        }

        cosecha.setEstado(EstadoCosecha.ACTIVA);

        // Al volver a ACTIVA ya no tiene fecha de finalización.
        cosecha.setFechaFinal(null);

        return cosechaMapper.toDto(
                cosechaRepository.save(cosecha)
        );
    }
}
