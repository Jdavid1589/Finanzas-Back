package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Cosecha;
import appsys.free.Agrocore.controlFinanzas.entities.Gasto;
import appsys.free.Agrocore.controlFinanzas.entities.Insumo;
import appsys.free.Agrocore.controlFinanzas.entities.TipoGasto;
import appsys.free.Agrocore.controlFinanzas.dtos.GastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.GastoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.GastoMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.CosechaRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.GastoRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.InsumoRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.TipoGastoRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.CosechaService;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.GastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GastoServiceImpl implements GastoService {

    private final GastoRepository gastoRepository;
    private final CosechaRepository cosechaRepository;
    private final InsumoRepository insumoRepository;
    private final TipoGastoRepository tipoGastoRepository;
    private final GastoMapper gastoMapper;
    // Se usa para disparar el recalculo de totales de la cosecha afectada
    // cada vez que un gasto cambia -- ver CosechaServiceImpl.recalcularTotales().
    private final CosechaService cosechaService;

    @Override
    @Transactional(readOnly = true)
    public List<GastoDTO> findAll() {
        return gastoMapper.toDtoList(gastoRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public GastoDTO findById(Long id) {
        return gastoMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public GastoDTO create(GastoRequestDTO dto) {
        Gasto gasto = gastoMapper.toEntity(dto);
        gasto.setCosecha(buscarCosecha(dto.cosechaId()));
        gasto.setTipoGasto(buscarTipoGasto(dto.tipoGastoId()));

        if (dto.insumoId() != null) {
            gasto.setInsumo(buscarInsumo(dto.insumoId()));
        }

        // total NO llega en el DTO -- se calcula aqui para que nunca quede
        // inconsistente con cantidad/costoInsumo.
        gasto.setTotal(dto.cantidad() * dto.costoInsumo());

        Gasto guardado = gastoRepository.save(gasto);

        // La cosecha necesita reflejar este gasto nuevo en sus totales.
        cosechaService.recalcularTotales(dto.cosechaId());

        return gastoMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public GastoDTO update(Long id, GastoRequestDTO dto) {
        Gasto gasto = buscarOFallar(id);
        Integer cosechaAnteriorId = gasto.getCosecha() != null ? gasto.getCosecha().getId() : null;

        gasto.setCantidad(dto.cantidad());
        gasto.setCostoInsumo(dto.costoInsumo());
        gasto.setDetalle(dto.detalle());
        gasto.setFecha(dto.fecha());
        gasto.setTipoFlete(dto.tipoFlete());
        gasto.setTipoServicio(dto.tipoServicio());
        gasto.setTotal(dto.cantidad() * dto.costoInsumo());
        gasto.setCosecha(buscarCosecha(dto.cosechaId()));
        gasto.setTipoGasto(buscarTipoGasto(dto.tipoGastoId()));
        gasto.setInsumo(dto.insumoId() != null ? buscarInsumo(dto.insumoId()) : null);

        Gasto guardado = gastoRepository.save(gasto);

        // Si el gasto se movio de cosecha, hay que recalcular AMBAS: la
        // que lo tenia antes (para quitarlo de sus totales) y la nueva
        // (para sumarlo). Si no cambio, es la misma llamada dos veces --
        // sin costo real, mantiene el codigo simple.
        cosechaService.recalcularTotales(dto.cosechaId());
        if (cosechaAnteriorId != null && !cosechaAnteriorId.equals(dto.cosechaId())) {
            cosechaService.recalcularTotales(cosechaAnteriorId);
        }

        return gastoMapper.toDto(guardado);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Gasto gasto = buscarOFallar(id);
        Integer cosechaId = gasto.getCosecha() != null ? gasto.getCosecha().getId() : null;

        gastoRepository.delete(gasto);

        if (cosechaId != null) {
            cosechaService.recalcularTotales(cosechaId);
        }
    }

    private Gasto buscarOFallar(Long id) {
        return gastoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Gasto no encontrado con id " + id));
    }

    private Cosecha buscarCosecha(Integer id) {
        return cosechaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cosecha no encontrada con id " + id));
    }

    private TipoGasto buscarTipoGasto(Integer id) {
        return tipoGastoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Tipo de gasto no encontrado con id " + id));
    }

    private Insumo buscarInsumo(Integer id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Insumo no encontrado con id " + id));
    }
}
