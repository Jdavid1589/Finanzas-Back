package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Employee;

import java.util.List;

public interface IEmployeeService {

    Employee addEmployeee(Employee employee);

    List<Employee> getEmployees(Boolean enable);

    boolean updateEmployee(Employee employee);

    Employee validNoDoc(String noDoc);





}
