package appsys.free.Agrocore.controlFinanzas.repositories;

import appsys.free.Agrocore.controlFinanzas.entities.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Integer> {

    Optional<Empleado> findByNoDocumento(String noDocumento);
}
