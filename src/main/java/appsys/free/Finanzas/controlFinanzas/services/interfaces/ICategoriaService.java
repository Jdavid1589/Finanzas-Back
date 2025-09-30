package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;

import java.util.List;
import java.util.Optional;

public interface ICategoriaService {

    List<CategoriaGastos> getAllCategorias();

    CategoriaGastos save (CategoriaGastos categoria);
    Optional<CategoriaGastos> findById(Long id);

    void deleteById(Long id);






}
