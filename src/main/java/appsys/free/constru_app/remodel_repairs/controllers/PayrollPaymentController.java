package appsys.free.constru_app.remodel_repairs.controllers;



import appsys.free.constru_app.remodel_repairs.dtos.PayrollValidationDto;
import appsys.free.constru_app.remodel_repairs.entities.PayrollPayment;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IPayrollPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;


@RestController
@RequestMapping("/payrollPayment/")    /*Pago Payroll*/
@CrossOrigin("*")
public class PayrollPaymentController {
    private static final Logger logger = LoggerFactory.getLogger(PayrollPaymentController.class);
    @Autowired
    IPayrollPaymentService iPayrollPaymentService;


    @Secured("ROLE_ADMIN")
    @PostMapping("add_Payment")
    public ResponseEntity<?> addPayment(@RequestBody PayrollPayment payrollPayment) {
        try {
            return new ResponseEntity<PayrollPayment>(iPayrollPaymentService.addParameter(payrollPayment), HttpStatus.OK);
        } catch (Exception e) {
            boolean resp = false;
            logger.error("ERROR PaymentController.add_Payment " + e.getMessage());
            return new ResponseEntity<Boolean>(resp, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    /* --- Metody para Actualize Parameters--- */
    @Secured("ROLE_ADMIN")
    @PostMapping("upd_Payment")
    public ResponseEntity<?> updatePayment(@RequestBody PayrollPayment payrollPayment) {
        try {
            // Intentar actualizar el pago
            boolean isUpdated = iPayrollPaymentService.updatePayrollPayment(payrollPayment);

            // Verificar si la actualización fue exitosa
            if (isUpdated) {
                return new ResponseEntity<>(true, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Pago no encontrado", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Registrar el error y devolver una respuesta de error interno del servidor
            logger.error("ERROR PaymentController.updatePayment: " + e.getMessage(), e);
            return new ResponseEntity<>("Error al actualizar el Pago", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Metody para Listar Parameters*/
    @Secured("ROLE_ADMIN")
    @GetMapping("PaymentsPayroll")
    public ResponseEntity<?> getPayments() {
        try {
            /*  service para oftener todos los pagos */
            List<PayrollPayment> paymentsList = iPayrollPaymentService.getPayment();

            /*  Si la list est vac, devel una status con 404*/
            if (paymentsList.isEmpty()) {
                return new ResponseEntity<>("No se encontraron Pagos", HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<>(paymentsList, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR en getParameters: " + e.getMessage());
            return new ResponseEntity<>("Error al obtener los pagos", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Metodo para Listar Pagos por Id
    @Secured("ROLE_ADMIN")
    @GetMapping("payment/{id}")
    public ResponseEntity<?> getPaymentById(@PathVariable int id) {
        try {
            PayrollPayment payment = iPayrollPaymentService.getPaymentById(id);
            if (payment == null) {
                return new ResponseEntity<>("Pago no encontrado", HttpStatus.NOT_FOUND);
            }
            // Devolver una lista con un solo elemento
            return new ResponseEntity<>(Collections.singletonList(payment), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("ERROR PaymenController.getWorkById: " + e.getMessage());
            return new ResponseEntity<>("Error al obtener el trabajo", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /*Metody para validator el Employee y Status*/
    // Método protegido por el rol "ROLE_ADMIN"
    @Secured("ROLE_ADMIN")
    @GetMapping("/validateNewPay/{idEmpl}/{status}")
    public ResponseEntity<PayrollValidationDto> validNewPayroll_(
            @PathVariable int idEmpl,
            @PathVariable boolean status) {
        try {
            // Llamada al servicio para validar la nómina del empleado
            PayrollValidationDto result = iPayrollPaymentService.validNewPayroll(idEmpl, status);

            // Log de información con el estado de la nómina
            logger.info("Payroll status for employee {}: {}", idEmpl, result);

            // Devolver una respuesta OK con el resultado de la validación
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            // Log de error si ocurre una excepción
            logger.error("Error PayrollPaymentController.validNewPayroll: " + e.getMessage(), e);

            // Devolver una respuesta de error interno del servidor
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



}
