package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoRequestDTO;

import java.util.List;

public interface EmpleadoService {

    List<EmpleadoDTO> findAll();

    EmpleadoDTO findById(Integer id);

    EmpleadoDTO create(EmpleadoRequestDTO dto);

    EmpleadoDTO update(Integer id, EmpleadoRequestDTO dto);

    void delete(Integer id);
}
