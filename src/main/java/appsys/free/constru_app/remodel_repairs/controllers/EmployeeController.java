package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.entities.Employee;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IBackService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

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



}
