package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.TipoGranel;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.TipoGranelMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.TipoGranelRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.TipoGranelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoGranelServiceImpl implements TipoGranelService {

    private final TipoGranelRepository tipoGranelRepository;
    private final TipoGranelMapper tipoGranelMapper;

    @Override
    public List<TipoGranelDTO> findAll() {
        return tipoGranelMapper.toDtoList(tipoGranelRepository.findAll());
    }

    @Override
    public TipoGranelDTO findById(Integer id) {
        return tipoGranelMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public TipoGranelDTO create(TipoGranelRequestDTO dto) {
        TipoGranel entidad = tipoGranelMapper.toEntity(dto);
        return tipoGranelMapper.toDto(tipoGranelRepository.save(entidad));
    }

    @Override
    @Transactional
    public TipoGranelDTO update(Integer id, TipoGranelRequestDTO dto) {
        TipoGranel entidad = buscarOFallar(id);
        entidad.setTipo(dto.tipo());
        return tipoGranelMapper.toDto(tipoGranelRepository.save(entidad));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        tipoGranelRepository.delete(buscarOFallar(id));
    }

    private TipoGranel buscarOFallar(Integer id) {
        return tipoGranelRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "TipoGranel no encontrado con id " + id));
    }
}
