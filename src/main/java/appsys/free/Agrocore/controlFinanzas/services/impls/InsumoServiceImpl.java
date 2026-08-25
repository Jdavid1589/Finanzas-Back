package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Insumo;
import appsys.free.Agrocore.controlFinanzas.dtos.InsumoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.InsumoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.InsumoMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.InsumoRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.InsumoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class InsumoServiceImpl implements InsumoService {

    private final InsumoRepository insumoRepository;
    private final InsumoMapper insumoMapper;

    @Override
    public List<InsumoDTO> findAll() {
        return insumoMapper.toDtoList(insumoRepository.findAll());
    }

    @Override
    public InsumoDTO findById(Integer id) {
        return insumoMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public InsumoDTO create(InsumoRequestDTO dto) {
        Insumo insumo = insumoMapper.toEntity(dto);
        return insumoMapper.toDto(insumoRepository.save(insumo));
    }

    @Override
    @Transactional
    public InsumoDTO update(Integer id, InsumoRequestDTO dto) {
        Insumo insumo = buscarOFallar(id);
        insumo.setNombre(dto.nombre());
        insumo.setCantidad(dto.cantidad());
        insumo.setCosto(dto.costo());
        return insumoMapper.toDto(insumoRepository.save(insumo));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        insumoRepository.delete(buscarOFallar(id));
    }

    private Insumo buscarOFallar(Integer id) {
        return insumoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Insumo no encontrado con id " + id));
    }
}
