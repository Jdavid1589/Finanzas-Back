package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.dtos.Requester_Dto;
import appsys.free.constru_app.remodel_repairs.dtos.SeeRequester_dto;
import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IBackService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IRequesterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/back")
@CrossOrigin(origins = "*")
public class BackController {

    private static final Logger logger=  LoggerFactory.getLogger(BackController.class);
    @Autowired
    IBackService iBackService;



    @Secured("ROLE_ADMIN")
    @GetMapping()
    public ResponseEntity<?> createBack() {
        try {
            boolean resp= iBackService.createBack();
            return  new ResponseEntity<Boolean>(resp, HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR createBack "+e.getMessage());
            return   new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }



}
