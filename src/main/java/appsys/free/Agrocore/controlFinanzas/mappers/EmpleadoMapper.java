package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Empleado;
import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoRequestDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpleadoMapper {

    EmpleadoDTO toDto(Empleado empleado);

    List<EmpleadoDTO> toDtoList(List<Empleado> empleados);

    Empleado toEntity(EmpleadoRequestDTO dto);
}
