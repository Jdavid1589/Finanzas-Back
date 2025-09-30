package appsys.free.Finanzas.controlFinanzas.repositories;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaIngresos;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICategIngresoRepo extends JpaRepository<CategoriaIngresos, Long> {

    Optional<CategoriaIngresos> findById(Long id);





}
