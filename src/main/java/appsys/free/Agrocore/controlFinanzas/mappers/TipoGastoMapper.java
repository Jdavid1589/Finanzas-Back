package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.TipoGasto;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoGastoMapper {

    TipoGastoDTO toDto(TipoGasto tipoGasto);

    List<TipoGastoDTO> toDtoList(List<TipoGasto> tipos);

    TipoGasto toEntity(TipoGastoRequestDTO dto);
}
