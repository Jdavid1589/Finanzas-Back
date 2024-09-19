package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.dtos.Material_dto;
import appsys.free.constru_app.remodel_repairs.dtos.SeeRequester_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IWorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/work/")
@CrossOrigin("*")
public class WorkController {
    private static final Logger logger=  LoggerFactory.getLogger(WorkController.class);
    @Autowired
    IWorkService iWorkService;

    @Secured("ROLE_ADMIN")
    @PostMapping("updWork")
    public ResponseEntity<?> updateWork(@RequestBody Work_Dto workDto) {
        try {

            return new ResponseEntity<Boolean>(iWorkService.updateWork(workDto), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR WorkController.updateWork "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("add_updWork")
    public ResponseEntity<?> add_updateWork(@RequestBody Work_Dto workDto) {
        try {

            return new ResponseEntity<Long>(iWorkService.add_updateWork(workDto), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR WorkController.add_updateWork "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("del_updWork")
    public ResponseEntity<?> delete_updateWork(@RequestBody Work_Dto workDto) {
        try {

            return new ResponseEntity<Boolean>(iWorkService.deleteWorkUpdate(workDto), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR WorkController.delete_updateWork "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("add_materUpd")
    public ResponseEntity<?> add_materUpd(@RequestBody Material_dto material_dto) {
        try {

            return new ResponseEntity<Long>(iWorkService.addMaterialWork(material_dto), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR WorkController.add_materUpd "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("del_materUpd")
    public ResponseEntity<?> del_materUpd(@RequestBody Material material) {
        try {

            return new ResponseEntity<Boolean>(iWorkService.del_materialUpd(material), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR WorkController.del_materUpd "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Secured("ROLE_ADMIN")
    @PostMapping("updMater")
    public ResponseEntity<?> updateMaterial(@RequestBody Material_dto material) {
        try {

            return new ResponseEntity<Boolean>(iWorkService.updateMateriaL(material), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR WorkController.updateMaterial "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

}
