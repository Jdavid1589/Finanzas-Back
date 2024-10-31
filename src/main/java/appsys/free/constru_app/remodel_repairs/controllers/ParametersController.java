package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.dtos.Material_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IParametersService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IWorkService;
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
@RequestMapping("/parameters/")
@CrossOrigin("*")
public class ParametersController {
    private static final Logger logger = LoggerFactory.getLogger(ParametersController.class);
    @Autowired
    IParametersService iParametersService;


    @Secured("ROLE_ADMIN")
    @PostMapping("add_Parameters")
    public ResponseEntity<?> addParameter(@RequestBody ParametersPayroll parameters) {
        try {
            return new ResponseEntity<ParametersPayroll>(iParametersService.addParameter(parameters), HttpStatus.OK);
        } catch (Exception e) {
            boolean resp = false;
            logger.error("ERROR ParametersController.add_Parameters " + e.getMessage());
            return new ResponseEntity<Boolean>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /* --- Metody para Actualize Parameters--- */
    @Secured("ROLE_ADMIN")
    @PostMapping("updParameters") // debe coincidir con la ruta completa
    public ResponseEntity<?> updateParameters(@RequestBody ParametersPayroll parameters) {
        try {
            boolean isUpdated = iParametersService.updateParameters(parameters);
            if (isUpdated) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Parametro no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("ERROR ParametersController.updateParameters: " + e.getMessage());
            return new ResponseEntity<>("Error al actualizar el parámetro", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Secured("ROLE_ADMIN")
    @PostMapping("updateSocialSecurityForAll")
    public ResponseEntity<?> updateSocialSecurityForAll(@RequestBody Map<String, Integer> request) {
        Integer socialSecurityValue = request.get("socialSecurity");
        try {
            boolean isUpdated = iParametersService.updateSocialSecurityForAll(socialSecurityValue);
            if (isUpdated) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No se encontraron parámetros para actualizar", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            logger.error("ERROR ParametersController.updateSocialSecurityForAll: " + e.getMessage());
            return new ResponseEntity<>("Error al actualizar el seguro social para todos los parámetros", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }




    /* Metody para Listar Parameters*/
    @Secured("ROLE_ADMIN")
    @GetMapping("parameter")
    public ResponseEntity<?> getParameters() {
        try {
            /*  service para oftener todos los parameters */
            List<ParametersPayroll> parametersList = iParametersService.getParameters();

            /*  Si la list est vac, devel una prestates con 404*/
            if (parametersList.isEmpty()) {
                return new ResponseEntity<>("No se encontraron parámetros", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(parametersList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR en getParameters: " + e.getMessage());
            return new ResponseEntity<>("Error al obtener los parámetros", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Metodo para Listar Parametros por Id
    @Secured("ROLE_ADMIN")
    @GetMapping("parameter/{id}")
    public ResponseEntity<?> getParametersById(@PathVariable int id) {
        try {
            ParametersPayroll parameters = iParametersService.getParametersById(id);
            if (parameters == null) {
                return new ResponseEntity<>("Parametro no encontrado", HttpStatus.NOT_FOUND);
            }
            // Devolver una lista con un solo elemento
            return new ResponseEntity<>(Collections.singletonList(parameters), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR WorkController.getWorkById: " + e.getMessage());
            return new ResponseEntity<>("Error al obtener el trabajo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
