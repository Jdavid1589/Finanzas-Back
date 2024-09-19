package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Departament;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IMunicipalityRepo extends JpaRepository<Municipality, Integer> {
    List<Municipality> findByDepartament(Departament departament);
}
