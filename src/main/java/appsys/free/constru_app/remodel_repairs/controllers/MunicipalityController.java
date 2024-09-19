package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IMunicipalityService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/municipality/")
@CrossOrigin(origins = "*")
public class MunicipalityController {

    private static final Logger logger=  LoggerFactory.getLogger(MunicipalityController.class);
    @Autowired
    IMunicipalityService iMunicipalityService;

    @Secured("ROLE_ADMIN")
    @GetMapping("{idDepartament}")
    public ResponseEntity<?> getMunicipalities(@PathVariable int idDepartament) {
        try {

            return  new ResponseEntity<List<Municipality>>(iMunicipalityService.getMunicipalities(idDepartament), HttpStatus.OK);
        }catch (Exception e){
            logger.error("ERROR MunicipalityController.getMunicipalities"+e.getMessage());
            return   new ResponseEntity<String>(e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);

        }


    }
}
