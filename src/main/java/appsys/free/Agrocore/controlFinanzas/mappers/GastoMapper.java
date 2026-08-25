package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Gasto;
import appsys.free.Agrocore.controlFinanzas.dtos.GastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.GastoRequestDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface GastoMapper {

    @Mapping(source = "cosecha.id", target = "cosechaId")
    @Mapping(source = "insumo.id", target = "insumoId")
    @Mapping(source = "tipoGasto.id", target = "tipoGastoId")
    GastoDTO toDto(Gasto gasto);

    List<GastoDTO> toDtoList(List<Gasto> gastos);

    @Mapping(target = "cosecha", ignore = true)
    @Mapping(target = "insumo", ignore = true)
    @Mapping(target = "tipoGasto", ignore = true)
    Gasto toEntity(GastoRequestDTO dto);
}
