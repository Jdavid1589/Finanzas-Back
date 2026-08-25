package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Cliente;
import appsys.free.Agrocore.controlFinanzas.dtos.ClienteDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.ClienteRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ClienteMapper {

    ClienteDTO toDto(Cliente cliente);

    List<ClienteDTO> toDtoList(List<Cliente> clientes);

    Cliente toEntity(ClienteRequestDTO dto);
}
