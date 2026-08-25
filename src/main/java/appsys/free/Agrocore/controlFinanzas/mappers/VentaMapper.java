package appsys.free.Agrocore.controlFinanzas.mappers;

import appsys.free.Agrocore.controlFinanzas.entities.Venta;
import appsys.free.Agrocore.controlFinanzas.dtos.VentaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.VentaRequestDTO;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface VentaMapper {

    @Mapping(source = "cosecha.id", target = "cosechaId")
    @Mapping(source = "cliente.id", target = "clienteId")
    VentaDTO toDto(Venta venta);

    List<VentaDTO> toDtoList(List<Venta> ventas);

    @Mapping(target = "cosecha", ignore = true)
    @Mapping(target = "cliente", ignore = true)
    // valorTotal no viene del request: se calcula en el service
    // (cantidad * valorUnidad) antes de guardar.
    Venta toEntity(VentaRequestDTO dto);
}
