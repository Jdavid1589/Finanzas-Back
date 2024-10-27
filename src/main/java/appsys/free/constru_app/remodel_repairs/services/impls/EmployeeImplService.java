package appsys.free.constru_app.remodel_repairs.services.impls;
import appsys.free.constru_app.remodel_repairs.dtos.Employee_Dto;
import appsys.free.constru_app.remodel_repairs.entities.*;
import appsys.free.constru_app.remodel_repairs.repositories.IEmployeeRepo;
import appsys.free.constru_app.remodel_repairs.repositories.IMunicipalityRepo;
import appsys.free.constru_app.remodel_repairs.repositories.IParametersRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import java.util.List;

@Service
public class EmployeeImplService implements IEmployeeService {
    private static final Logger logger = LoggerFactory.getLogger(EmployeeImplService.class);

    @Autowired
    IEmployeeRepo iEmployeeRepo;
    @Autowired
    IMunicipalityRepo iMunicipalityRepo;
    @Autowired
    IParametersRepo iParametersRepo;

   /* @Override
    public Employee addEmployeee(Employee employee) {
        return iEmployeeRepo.save(employee);

    }*/

    @Override
    public Employee addEmployeees(Employee_Dto employeeDto) {
        Employee employee = new Employee();

        employee.setDocumentNumber(employeeDto.getDocumentNumber());
        employee.setNames(employeeDto.getNames());
        employee.setSurnames(employeeDto.getSurnames());
        employee.setPhoneNumber(employeeDto.getPhoneNumber());
        employee.setAddress(employeeDto.getAddress());
        employee.setEmail(employeeDto.getEmail());
        employee.setEnable(employeeDto.isEnable());

        // Puedes buscar la municipalidad y ParametersPayroll a partir de sus IDs
        Municipality municipality = iMunicipalityRepo.findById(employeeDto.getIdMunicipality())
                .orElseThrow(() -> new IllegalArgumentException("Municipality not found"));
        employee.setMunicipality(municipality);

        ParametersPayroll parametersPayroll = iParametersRepo.findById(employeeDto.getIdParametersPayroll())
                .orElseThrow(() -> new IllegalArgumentException("ParametersPayroll not found"));
        employee.setParametersPayroll(parametersPayroll);

        return iEmployeeRepo.save(employee);
    }




    @Override
    public List<Employee> getEmployees(Boolean enable) {
        return iEmployeeRepo.findByEnable(enable);
    }

    @Override
    public List<Employee> getDisabledEmployees() {
        return iEmployeeRepo.findByEnable(false); // Esto debe devolver empleados con enable = false
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
            existingEmployee.setParametersPayroll(employee.getParametersPayroll());
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
