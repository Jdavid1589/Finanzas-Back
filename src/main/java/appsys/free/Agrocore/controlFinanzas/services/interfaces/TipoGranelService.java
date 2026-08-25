package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelRequestDTO;

import java.util.List;

public interface TipoGranelService {

    List<TipoGranelDTO> findAll();

    TipoGranelDTO findById(Integer id);

    TipoGranelDTO create(TipoGranelRequestDTO dto);

    TipoGranelDTO update(Integer id, TipoGranelRequestDTO dto);

    void delete(Integer id);
}
