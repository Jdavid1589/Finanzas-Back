package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.entities.Employee;
import appsys.free.constru_app.remodel_repairs.repositories.IEmployeeRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IBackService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
}
