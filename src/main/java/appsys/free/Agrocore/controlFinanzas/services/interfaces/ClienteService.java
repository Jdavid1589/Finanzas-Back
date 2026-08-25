package appsys.free.Agrocore.controlFinanzas.services.interfaces;

import appsys.free.Agrocore.controlFinanzas.dtos.ClienteDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.ClienteRequestDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> findAll();

    ClienteDTO findById(Integer id);

    ClienteDTO create(ClienteRequestDTO dto);

    ClienteDTO update(Integer id, ClienteRequestDTO dto);

    void delete(Integer id);
}
