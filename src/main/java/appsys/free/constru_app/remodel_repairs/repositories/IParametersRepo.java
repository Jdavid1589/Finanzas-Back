package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IParametersRepo extends JpaRepository<ParametersPayroll,Integer> {

}
