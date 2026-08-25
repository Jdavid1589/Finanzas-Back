package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.InsumoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.InsumoRequestDTO;

import java.util.List;

public interface InsumoService {

    List<InsumoDTO> findAll();

    InsumoDTO findById(Integer id);

    InsumoDTO create(InsumoRequestDTO dto);

    InsumoDTO update(Integer id, InsumoRequestDTO dto);

    void delete(Integer id);
}
