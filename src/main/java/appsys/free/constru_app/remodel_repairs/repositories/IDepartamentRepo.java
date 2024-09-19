package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Departament;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IDepartamentRepo extends JpaRepository<Departament,Integer> {
}
