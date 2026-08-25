package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Insumo;
import appsys.free.Agrocore.controlFinanzas.dtos.InsumoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.InsumoRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface InsumoMapper {

    InsumoDTO toDto(Insumo insumo);

    List<InsumoDTO> toDtoList(List<Insumo> insumos);

    Insumo toEntity(InsumoRequestDTO dto);
}
