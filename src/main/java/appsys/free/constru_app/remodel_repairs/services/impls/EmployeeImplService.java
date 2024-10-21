package appsys.free.constru_app.remodel_repairs.services.impls;
import appsys.free.constru_app.remodel_repairs.entities.*;
import appsys.free.constru_app.remodel_repairs.repositories.IEmployeeRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class EmployeeImplService implements IEmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeImplService.class);

    @Autowired
    IEmployeeRepo iEmployeeRepo;

    @Override
    public Employee addEmployeee(Employee employee) {
        return iEmployeeRepo.save(employee);

    }

    @Override
    public List<Employee> getEmployees(Boolean enable) {
        return iEmployeeRepo.findByEnable(enable);
    }

    @Override
    public List<Employee> getDisabledEmployees() {
        return iEmployeeRepo.findByEnable(false); // Solo devuelve empleados deshabilitados
    }



    @Transactional
    @Override
    public boolean updateEmployee(Employee employee) {
        Optional<Employee> EmpltUpd = iEmployeeRepo.findById(employee.getId());
        if (EmpltUpd.isPresent()) {
            Employee existingEmployee = EmpltUpd.get();

            // Actualizar los campos del empleado
            existingEmployee.setNames(employee.getNames());
            existingEmployee.setSurnames(employee.getSurnames());
            existingEmployee.setDocumentNumber(employee.getDocumentNumber());
            existingEmployee.setAddress(employee.getAddress());
            existingEmployee.setEmail(employee.getEmail());
            existingEmployee.setMunicipality(employee.getMunicipality());
            existingEmployee.setPhoneNumber(employee.getPhoneNumber());
            existingEmployee.setEnable(employee.isEnable());

            iEmployeeRepo.save(existingEmployee);  // Guardar los cambios en la BD
            return true;  // Actualización exitosa
        }
        return false;  // El empleado no fue encontrado
    }

    @Override
    public Employee validNoDoc(String noDoc) {
        Optional<Employee> employee = iEmployeeRepo.findByDocumentNumber(noDoc);
        if (employee.isPresent()) {
            return employee.get();
        }
        return null;
    }

    @Transactional
    @Override
    public boolean statusEmployee(int idEmployee) {
        Optional<Employee> employeeOpt = iEmployeeRepo.findById(idEmployee);
        if (employeeOpt.isPresent()) {
            Employee employee = employeeOpt.get();

            // Cambiar el estado de enable
            employee.setEnable(!employee.isEnable());  // Cambia el estado a su valor opuesto

            iEmployeeRepo.save(employee);  // Guardar los cambios en la BD
            return true;  // Actualización exitosa
        }
        return false;  // El empleado no fue encontrado
    }



}
