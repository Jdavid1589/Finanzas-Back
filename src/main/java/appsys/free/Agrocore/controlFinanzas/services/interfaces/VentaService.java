package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.VentaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.VentaRequestDTO;

import java.util.List;

public interface VentaService {

    List<VentaDTO> findAll();

    VentaDTO findById(Long id);

    VentaDTO create(VentaRequestDTO dto);

    VentaDTO update(Long id, VentaRequestDTO dto);

    void delete(Long id);
}
