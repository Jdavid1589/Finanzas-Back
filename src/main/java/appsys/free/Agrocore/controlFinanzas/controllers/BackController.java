package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.services.interfaces.IBackService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/back")
@CrossOrigin(origins = "*")
public class BackController {

    private static final Logger logger = LoggerFactory.getLogger(BackController.class);
    @Autowired
    IBackService iBackService;



    @GetMapping()
    public ResponseEntity<?> createBack() {
        try {
            boolean resp = iBackService.createBack();
            return new ResponseEntity<Boolean>(resp, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR createBack " + e.getMessage());
            return new ResponseEntity<String>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }


}
