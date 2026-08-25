package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.NominaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.NominaRequestDTO;

import java.util.List;

public interface NominaService {

    List<NominaDTO> findAll();

    NominaDTO findById(Long id);

    NominaDTO create(NominaRequestDTO dto);

    NominaDTO update(Long id, NominaRequestDTO dto);

    void delete(Long id);
}
