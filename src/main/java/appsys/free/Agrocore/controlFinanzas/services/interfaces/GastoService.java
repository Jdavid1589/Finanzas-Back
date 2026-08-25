package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.GastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.GastoRequestDTO;

import java.util.List;

public interface GastoService {

    List<GastoDTO> findAll();

    GastoDTO findById(Long id);

    GastoDTO create(GastoRequestDTO dto);

    GastoDTO update(Long id, GastoRequestDTO dto);

    void delete(Long id);
}
