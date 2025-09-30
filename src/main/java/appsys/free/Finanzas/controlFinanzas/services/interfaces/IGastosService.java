package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.dtos.Gastos_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;

import java.util.List;
import java.util.Optional;

public interface IGastosService {
    Optional<Gastos> getGastosById(Long id);

    List<Gastos> getAllGastos();

    Gastos save(Gastos_Dto gastos);

    boolean existsById(Long id);

    void deleteById(Long id);





}
