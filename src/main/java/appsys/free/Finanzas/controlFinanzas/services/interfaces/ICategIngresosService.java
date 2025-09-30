package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaIngresos;

import java.util.List;
import java.util.Optional;

public interface ICategIngresosService {

    List<CategoriaIngresos> getAllCategorias();

    void save (CategoriaIngresos categoria);
    Optional<CategoriaIngresos> findById(Long id);

    void deleteById(Long id);






}
