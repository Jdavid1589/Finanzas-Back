package appsys.free.Agrocore.controlFinanzas.repositories;

import appsys.free.Agrocore.controlFinanzas.entities.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NominaRepository extends JpaRepository<Nomina, Long> {

    List<Nomina> findByCosechaId(Integer cosechaId);

    List<Nomina> findByEmpleadoId(Integer empleadoId);

    List<Nomina> findByEstadoPago(Boolean estadoPago);
}
