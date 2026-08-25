package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoRequestDTO;

import java.util.List;

public interface TipoGastoService {

    List<TipoGastoDTO> findAll();

    TipoGastoDTO findById(Integer id);

    TipoGastoDTO create(TipoGastoRequestDTO dto);

    TipoGastoDTO update(Integer id, TipoGastoRequestDTO dto);

    void delete(Integer id);
}
