package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface IIngresosService {

    List<Ingresos> getAllIngresos();

   Ingresos save (Ingreso_Dto ingresos);

    Optional<Ingresos> getIngresoById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);





}
