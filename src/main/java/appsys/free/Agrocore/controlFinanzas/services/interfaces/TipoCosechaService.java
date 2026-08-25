package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaRequestDTO;

import java.util.List;

public interface TipoCosechaService {

    List<TipoCosechaDTO> findAll();

    TipoCosechaDTO findById(Integer id);

    TipoCosechaDTO create(TipoCosechaRequestDTO dto);

    TipoCosechaDTO update(Integer id, TipoCosechaRequestDTO dto);

    void delete(Integer id);
}
