package appsys.free.constru_app.remodel_repairs.services.impls;
import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Employee;
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


    @Transactional
    @Override
    public boolean updateEmployeee(Employee employee) {
        Optional<Employee> EmpltUpd=iEmployeeRepo.findById(employee.getId());
        if(EmpltUpd.isPresent()){
            EmpltUpd.get().setNames(employee.getNames());
            EmpltUpd.get().setSurnames(employee.getSurnames());
            EmpltUpd.get().setDocumentNumber(employee.getDocumentNumber());
            EmpltUpd.get().setAddress(employee.getAddress());
            EmpltUpd.get().setEmail(employee.getEmail());
            EmpltUpd.get().setMunicipality(employee.getMunicipality());
            EmpltUpd.get().setPhoneNumber(employee.getPhoneNumber());

            EmpltUpd.get().setEnable(employee.isEnable());


            iEmployeeRepo.save(EmpltUpd.get());
        }
        return false;
    }

    @Override
    public Employee validNoDoc(String noDoc) {
        Optional<Employee> employee = iEmployeeRepo.findByDocumentNumber(noDoc);
        if (employee.isPresent()) {
            return employee.get();
        }
        return null;
    }


}
