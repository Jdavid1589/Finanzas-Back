package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Cosecha;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaRequestDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CosechaMapper {

    @Mapping(source = "tipoCosecha.id", target = "tipoCosechaId")
    @Mapping(source = "tipoCosecha.tipo", target = "tipoCosechaNombre")
    @Mapping(source = "tipoGranel.id", target = "tipoGranelId")
    @Mapping(source = "tipoGranel.tipo", target = "tipoGranelNombre")
    CosechaDTO toDto(Cosecha cosecha);

    List<CosechaDTO> toDtoList(List<Cosecha> cosechas);

    // tipoCosecha y tipoGranel quedan sin mapear a proposito (el
    // RequestDTO solo trae el id, no el objeto) -- el service los busca
    // por id y los asigna con cosecha.setTipoCosecha(...) antes de guardar.
    // Los campos total_* tampoco vienen del DTO: quedan en su valor por
    // defecto (0) y el service decide como calcularlos.
    @Mapping(target = "tipoCosecha", ignore = true)
    @Mapping(target = "tipoGranel", ignore = true)
    Cosecha toEntity(CosechaRequestDTO dto);
}
