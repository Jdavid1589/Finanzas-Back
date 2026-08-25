package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.TipoCosecha;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.TipoCosechaMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.TipoCosechaRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.TipoCosechaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TipoCosechaServiceImpl implements TipoCosechaService {

    private final TipoCosechaRepository tipoCosechaRepository;
    private final TipoCosechaMapper tipoCosechaMapper;

    @Override
    public List<TipoCosechaDTO> findAll() {
        return tipoCosechaMapper.toDtoList(tipoCosechaRepository.findAll());
    }

    @Override
    public TipoCosechaDTO findById(Integer id) {
        return tipoCosechaMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public TipoCosechaDTO create(TipoCosechaRequestDTO dto) {
        TipoCosecha entidad = tipoCosechaMapper.toEntity(dto);
        return tipoCosechaMapper.toDto(tipoCosechaRepository.save(entidad));
    }

    @Override
    @Transactional
    public TipoCosechaDTO update(Integer id, TipoCosechaRequestDTO dto) {
        TipoCosecha entidad = buscarOFallar(id);
        entidad.setTipo(dto.tipo());
        return tipoCosechaMapper.toDto(tipoCosechaRepository.save(entidad));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        tipoCosechaRepository.delete(buscarOFallar(id));
    }

    private TipoCosecha buscarOFallar(Integer id) {
        return tipoCosechaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "TipoCosecha no encontrado con id " + id));
    }
}
