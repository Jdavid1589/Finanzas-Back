package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Cliente;
import appsys.free.Agrocore.controlFinanzas.dtos.ClienteDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.ClienteRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.ClienteMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.ClienteRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteServiceImpl implements ClienteService {

    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;

    @Override
    public List<ClienteDTO> findAll() {
        return clienteMapper.toDtoList(clienteRepository.findAll());
    }

    @Override
    public ClienteDTO findById(Integer id) {
        return clienteMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public ClienteDTO create(ClienteRequestDTO dto) {
        Cliente cliente = clienteMapper.toEntity(dto);
        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public ClienteDTO update(Integer id, ClienteRequestDTO dto) {
        Cliente cliente = buscarOFallar(id);
        cliente.setNombre(dto.nombre());
        cliente.setNit(dto.nit());
        cliente.setCorreo(dto.correo());
        cliente.setTelefonos(dto.telefonos());
        return clienteMapper.toDto(clienteRepository.save(cliente));
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        clienteRepository.delete(buscarOFallar(id));
    }

    private Cliente buscarOFallar(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con id " + id));
    }
}
