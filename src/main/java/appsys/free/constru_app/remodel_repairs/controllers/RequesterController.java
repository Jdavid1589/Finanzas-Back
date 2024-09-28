package appsys.free.constru_app.remodel_repairs.controllers;

import appsys.free.constru_app.remodel_repairs.dtos.Requester_Dto;
import appsys.free.constru_app.remodel_repairs.dtos.SeeRequester_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IMunicipalityService;
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
@RequestMapping("/request/")
@CrossOrigin(origins = "*")
public class RequesterController {

    private static final Logger logger=  LoggerFactory.getLogger(RequesterController.class);
    @Autowired
    IRequesterService iRequesterService;

    @Secured("ROLE_ADMIN")
    @PostMapping("savReq")
    public void addRequester(HttpServletResponse response, @RequestBody Requester_Dto requesterDto) {
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=request.pdf";
            response.setHeader(headerKey, headerValue);

            iRequesterService.addRequester(response,requesterDto);
        }catch (Exception e){
            logger.error("ERROR RequesterController.addRequester"+e.getMessage());


        }


    }

    @Secured("ROLE_ADMIN")
    @GetMapping("seeReq/{idStatus}")
    public ResponseEntity<?> seeRequests(@PathVariable int idStatus) {
        try {

            return new ResponseEntity<List<SeeRequester_dto>>(iRequesterService.getRequests(idStatus),HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR RequesterController.seeRequests "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("delReq/{idRequest}")
    public ResponseEntity<?> delRequests(@PathVariable int idRequest) {
        try {

            return new ResponseEntity<Boolean>(iRequesterService.deleteRequest(idRequest),HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR RequesterController.delRequests "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("validNoDoc/{noDoc}")
    public ResponseEntity<?> validNoDoc(@PathVariable String noDoc) {
        try {

            return new ResponseEntity<Customer>(iRequesterService.validNoDoc(noDoc), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR RequesterController.validNoDoc "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Secured("ROLE_ADMIN")
    @PostMapping("addupdEquip/{idRequ}")
    public ResponseEntity<?> addUpdateMaterial(@RequestBody Equipment equipment,@PathVariable int idRequ) {
        try {

            return new ResponseEntity<Integer>(iRequesterService.addUpdateEquipment(equipment,idRequ), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR RequesterController.addUpdateMaterial "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Secured("ROLE_ADMIN")
    @GetMapping("delUpdEquip/{idEquip}")
    public ResponseEntity<?> deleteUpdateEquipment(@PathVariable int idEquip) {
        try {

            return new ResponseEntity<Boolean>(iRequesterService.deleteUpdateEquipment(idEquip),HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.deleteUpdateEquipment "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("updEquip")
    public ResponseEntity<?> updateEquipment(@RequestBody Equipment equipment) {
        try {

            return new ResponseEntity<Boolean>(iRequesterService.updateEquipment(equipment),HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.updateEquipment "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("addUpdTransp/{idRequ}")
    public ResponseEntity<?> addUpdateTransp(@RequestBody Transport transport, @PathVariable int idRequ) {
        try {

            return new ResponseEntity<Integer>(iRequesterService.addUpdateTransport(transport,idRequ), HttpStatus.OK);
        }catch (Exception e){
            boolean resp= false;
            logger.error("ERROR RequesterController.addUpdateTransp "+e.getMessage());
            return new ResponseEntity<Boolean>(resp,HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

    @Secured("ROLE_ADMIN")
    @GetMapping("delUpdTransp/{idTransp}")
    public ResponseEntity<?> deleteUpdateTransport(@PathVariable int idTransp) {
        try {

            return new ResponseEntity<Boolean>(iRequesterService.deleteUpdateTransport(idTransp),HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.deleteUpdateTransport "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @PostMapping("updTransp")
    public ResponseEntity<?> updateTransport(@RequestBody Transport transport) {
        try {

            return new ResponseEntity<Boolean>(iRequesterService.updateTransport(transport),HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.updateTransport "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("startWork/{idRequest}")
    public ResponseEntity<?> startWorks(@PathVariable int idRequest) {
        try {

            return new ResponseEntity<Boolean>(iRequesterService.startWorks(idRequest),HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.startWorks "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Secured("ROLE_ADMIN")
    @GetMapping("reprintReq/{idRequ}")
    public void reprintRequester(HttpServletResponse response, @PathVariable int idRequ) {
        try {
            response.setContentType("application/octet-stream");
            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=request.pdf";
            response.setHeader(headerKey, headerValue);

            iRequesterService.reprintRequester(response,idRequ);
        }catch (Exception e){
            logger.error("ERROR RequesterController.reprintRequester"+e.getMessage());


        }


    }

    @Secured("ROLE_ADMIN")
    @GetMapping("getUpdRequ/{idRequ}")
    public ResponseEntity<?> getUpdRequ(@PathVariable int idRequ) {
        try {

            return new ResponseEntity<Requester_Dto>(iRequesterService.getRequestById(idRequ), HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.getUpdRequ "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }


    @Secured("ROLE_ADMIN")
    @PostMapping("updRequ")
    public ResponseEntity<?> updRequ(@RequestBody Requester_Dto requesterDto) {
        try {

            return new ResponseEntity<Requester_Dto>(iRequesterService.updateRequest(requesterDto), HttpStatus.OK);
        }catch (Exception e){

            logger.error("ERROR RequesterController.updRequ "+e.getMessage());
            return new ResponseEntity<Boolean>(false,HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}
