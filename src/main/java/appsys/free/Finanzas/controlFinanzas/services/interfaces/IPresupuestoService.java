package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.dtos.Gastos_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.PresupuestoDto;
import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.entities.Presupuesto;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface IPresupuestoService {

    Presupuesto save(PresupuestoDto presupuesto);

    boolean existsById(Long id);

    Optional<Presupuesto> getPresupById(Long id);

    void deleteById(Long id);

    List<Presupuesto> getAllPresupuesto();

    void eliminarPresupuesto(Long id);

    PresupuestoDto obtenerPresupuesto(Long id);

    List<PresupuestoDto> listarPresupuestos();



    // 🔹 Cambiar a DTO  // Metodo OK
    List<SaldoPresupuestoDTO> getPresupuestoPorCategoria();

    List<SaldoPresupuestoDTO> getPresupuestoPorMes();

    List<SaldoPresupuestoDTO> getPresupuestoPorMesYCategoria();







}
