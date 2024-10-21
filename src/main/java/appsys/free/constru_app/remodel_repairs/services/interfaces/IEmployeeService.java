package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Employee;

import java.util.List;

public interface IEmployeeService {

    Employee addEmployeee(Employee employee);

    List<Employee> getEmployees(Boolean enable);

    List<Employee> getDisabledEmployees();

    boolean updateEmployee(Employee employee);
    boolean statusEmployee(int idEmployee);

    Employee validNoDoc(String noDoc);





}
