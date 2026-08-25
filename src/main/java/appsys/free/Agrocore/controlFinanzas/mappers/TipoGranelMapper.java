package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.TipoGranel;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoGranelMapper {

    TipoGranelDTO toDto(TipoGranel tipoGranel);

    List<TipoGranelDTO> toDtoList(List<TipoGranel> tipos);

    TipoGranel toEntity(TipoGranelRequestDTO dto);
}
