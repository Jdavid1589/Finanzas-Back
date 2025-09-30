package appsys.free.Finanzas.controlFinanzas.repositories;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategoriaRepo extends JpaRepository<CategoriaGastos, Long> {

    Optional<CategoriaGastos> findById(Long id);

    Optional<CategoriaGastos> findByNombreCategoria(String nombreCategoria);





}
