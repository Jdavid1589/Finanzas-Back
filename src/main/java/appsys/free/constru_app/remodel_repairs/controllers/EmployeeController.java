package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Employee;

import appsys.free.constru_app.remodel_repairs.entities.Transport;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/empl")
@CrossOrigin(origins = "*")
public class EmployeeController {

    private static final Logger logger=  LoggerFactory.getLogger(EmployeeController.class);
    @Autowired
    IEmployeeService iEmployeeService;


    @Secured("ROLE_ADMIN")
    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {
        try {
            return  new ResponseEntity<Employee>(iEmployeeService.addEmployeee(employee), HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR addEmployee "+e.getMessage());
            return   new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @GetMapping("/listEmp/{enable}")
    public ResponseEntity<?> getEmployees(@PathVariable boolean enable) {
        try {
            return  new ResponseEntity<List<Employee>>(iEmployeeService.getEmployees(enable), HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR getEmployees "+e.getMessage());
            return   new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("updEmp/{id}")
    public ResponseEntity<?> updateEmployee(@PathVariable int id, @RequestBody Employee employee) {
        try {
            employee.setId(id);  // Establecer el ID del empleado
            boolean isUpdated = iEmployeeService.updateEmployee(employee);
            return new ResponseEntity<>(isUpdated, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR RequesterController.updateEmployee: " + e.getMessage());
            return new ResponseEntity<>(false, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }





    @Secured("ROLE_ADMIN")
    @GetMapping("validNoDoc/{noDoc}")
    public ResponseEntity<?> validNoDoc(@PathVariable String noDoc) {
        try {
            return new ResponseEntity<Employee>(iEmployeeService.validNoDoc(noDoc), HttpStatus.OK);

        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR RequesterController.validNoDoc "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }



}
