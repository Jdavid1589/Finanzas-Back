package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.TipoGasto;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.TipoGastoMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.TipoGastoRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.TipoGastoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoGastoServiceImpl implements TipoGastoService {

    private final TipoGastoRepository tipoGastoRepository;
    private final TipoGastoMapper tipoGastoMapper;

    @Override
    public List<TipoGastoDTO> findAll() {
        return tipoGastoMapper.toDtoList(tipoGastoRepository.findAll());
    }

    @Override
    public TipoGastoDTO findById(Integer id) {
        return tipoGastoMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public TipoGastoDTO create(TipoGastoRequestDTO dto) {
        TipoGasto entidad = tipoGastoMapper.toEntity(dto);
        return tipoGastoMapper.toDto(tipoGastoRepository.save(entidad));
    }

    @Override
    @Transactional
    public TipoGastoDTO update(Integer id, TipoGastoRequestDTO dto) {
        TipoGasto entidad = buscarOFallar(id);
        entidad.setTipo(dto.tipo());
        return tipoGastoMapper.toDto(tipoGastoRepository.save(entidad));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        tipoGastoRepository.delete(buscarOFallar(id));
    }

    private TipoGasto buscarOFallar(Integer id) {
        return tipoGastoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "TipoGasto no encontrado con id " + id));
    }
}
