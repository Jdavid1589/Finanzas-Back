package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import appsys.free.constru_app.remodel_repairs.entities.TypePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITypePayrollRepo extends JpaRepository<TypePayroll,Integer> {

}
