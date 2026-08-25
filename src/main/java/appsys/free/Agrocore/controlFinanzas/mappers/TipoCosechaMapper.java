package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.TipoCosecha;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface TipoCosechaMapper {

    TipoCosechaDTO toDto(TipoCosecha tipoCosecha);

    List<TipoCosechaDTO> toDtoList(List<TipoCosecha> tipos);

    TipoCosecha toEntity(TipoCosechaRequestDTO dto);
}
