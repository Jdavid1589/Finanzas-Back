package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Empleado;
import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.EmpleadoMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.EmpleadoRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.EmpleadoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmpleadoServiceImpl implements EmpleadoService {

    private final EmpleadoRepository empleadoRepository;
    private final EmpleadoMapper empleadoMapper;

    @Override
    public List<EmpleadoDTO> findAll() {
        return empleadoMapper.toDtoList(empleadoRepository.findAll());
    }

    @Override
    public EmpleadoDTO findById(Integer id) {
        return empleadoMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public EmpleadoDTO create(EmpleadoRequestDTO dto) {
        Empleado empleado = empleadoMapper.toEntity(dto);
        if (empleado.getEnable() == null) {
            empleado.setEnable(true);
        }
        return empleadoMapper.toDto(empleadoRepository.save(empleado));
    }

    @Override
    @Transactional
    public EmpleadoDTO update(Integer id, EmpleadoRequestDTO dto) {
        Empleado empleado = buscarOFallar(id);
        empleado.setNombres(dto.nombres());
        empleado.setNoDocumento(dto.noDocumento());
        empleado.setCorreo(dto.correo());
        empleado.setTelefonos(dto.telefonos());
        if (dto.enable() != null) {
            empleado.setEnable(dto.enable());
        }
        return empleadoMapper.toDto(empleadoRepository.save(empleado));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        empleadoRepository.delete(buscarOFallar(id));
    }

    private Empleado buscarOFallar(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empleado no encontrado con id " + id));
    }
}
