package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Nomina;
import appsys.free.Agrocore.controlFinanzas.dtos.NominaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.NominaRequestDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NominaMapper {

    @Mapping(source = "cosecha.id", target = "cosechaId")
    @Mapping(source = "empleado.id", target = "empleadoId")
    @Mapping(source = "cosecha.tipoGranel.id", target = "tipoGranelId")
    @Mapping(source = "cosecha.tipoGranel.tipo", target = "tipoGranelNombre")
    NominaDTO toDto(Nomina nomina);

    List<NominaDTO> toDtoList(List<Nomina> nominas);

    @Mapping(target = "cosecha", ignore = true)
    @Mapping(target = "empleado", ignore = true)
    Nomina toEntity(NominaRequestDTO dto);
}