package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import appsys.free.constru_app.remodel_repairs.entities.Workday;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IParametersService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IWorkdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/workdays/")
@CrossOrigin("*")
public class WorkdayController {
    private static final Logger logger = LoggerFactory.getLogger(WorkdayController.class);
    @Autowired
    IWorkdayService iWorkdayService;


    /* Metody para Listar todos los  Workday*/
    @Secured("ROLE_ADMIN")
    @GetMapping("listworkdays")
    public ResponseEntity<?> getWorkday() {
        try {
            /*  service para oftener todos los parameters */
            List<Workday> workdayList = iWorkdayService.getWorkday();

            /*  Si la list est vac, devel una prestates con 404*/
            if (workdayList.isEmpty()) {
                return new ResponseEntity<>("No se encontraron parámetros", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(workdayList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR en getParameters: " + e.getMessage());
            return new ResponseEntity<>("Error al obtener los parámetros", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   /*  Metody para Listar Workday por ID*/
    @Secured("ROLE_ADMIN")
    @GetMapping("workday/{id}")
    public ResponseEntity<?> getWorkdayById(@PathVariable int id) {
        try {
            Workday workday = iWorkdayService.getWorkdayById(id);
            if (workday == null) {
                return new ResponseEntity<>("Parameter no encontrado", HttpStatus.NOT_FOUND);
            }
            // Devolve una list con un solo element
            return new ResponseEntity<>(Collections.singletonList(workday), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR WorkController.getWorkById: " + e.getMessage());
            return new ResponseEntity<>("Error al obtener el trabajo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
