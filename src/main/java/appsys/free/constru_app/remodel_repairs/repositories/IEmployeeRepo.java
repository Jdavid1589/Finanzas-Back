package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IEmployeeRepo extends JpaRepository<Employee,Integer> {


    List<Employee> findByEnable(boolean enable);
    //List<Employee> getDisabledEmployees();


    Optional<Employee> findByDocumentNumber(String documentNumber);
}
