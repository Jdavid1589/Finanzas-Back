package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.entities.Employee;
import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import appsys.free.constru_app.remodel_repairs.entities.PayrollPayment;
import appsys.free.constru_app.remodel_repairs.entities.TypePayroll;
import appsys.free.constru_app.remodel_repairs.repositories.IParametersRepo;
import appsys.free.constru_app.remodel_repairs.repositories.IPayrollPaymentRepo;
import appsys.free.constru_app.remodel_repairs.responses.PayrollValidationResponse;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IParametersService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IPayrollPaymentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollPaymentImplService implements IPayrollPaymentService {

    private static final Logger logger = LoggerFactory.getLogger(PayrollPaymentImplService.class);
    @Autowired
    IPayrollPaymentRepo iPayrollPaymentRepo;

    @Override
    public List<PayrollPayment> getPayment() {
        try {
            // Recuperar todos los pagos de nómina desde el repositorio
            return iPayrollPaymentRepo.findAll();
        } catch (Exception e) {
            // Manejo de excepciones: Registrar o manejar la excepción según sea necesario
            System.err.println("Error retrieving payroll payments: " + e.getMessage());
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    /*metodo validar ok */
   /* @Override
    public boolean validNewPayroll(int idEmployed, boolean paymentStatus) {
        // Buscar el empleado y su estado de pago en la base de datos
        Optional<PayrollPayment> resp = iPayrollPaymentRepo.findByEmployeeAndPaymentStatus(new Employee(idEmployed), paymentStatus);

        // Si el registro existe
        if (resp.isPresent()) {
            // Si el estado de pago es true, devolver true
            if (resp.get().isPaymentStatus()) {
                return true;
            }
            // Si el estado de pago es false, devolver false
            return false;
        }

        // Si no existe el registro, devolver true para permitir un nuevo registro
        return true;
    }
*/

    @Override
    public PayrollValidationResponse validNewPayroll_(int idEmployed, boolean paymentStatus) {
        // Buscar el empleado y su estado de pago en la base de datos
        Optional<PayrollPayment> resp = iPayrollPaymentRepo.findByEmployee(new Employee(idEmployed));

        // Si el registro existe
        if (resp.isPresent()) {
            PayrollPayment payrollPayment = resp.get();
            // Verificar el estado de pago
            boolean currentPaymentStatus = payrollPayment.isPaymentStatus();
            int typePayrollId = payrollPayment.getTypePayroll().getId();
            return new PayrollValidationResponse(currentPaymentStatus, typePayrollId);
        }

        // Si no existe el registro, devolver true para permitir un nuevo registro
        return new PayrollValidationResponse(true, 0); // 0 indica que no hay tipo de nómina asociado
    }

    @Override
    public PayrollPayment getPaymentById(int id) {
        // Buscar el Pago en la base de datos por id
        Optional<PayrollPayment> paymentOptional = iPayrollPaymentRepo.findById(id);

        // Si no se encuentra, devolver null
        if (paymentOptional.isEmpty()) {
            return null;
        }

        // Retornar el objeto encontrado
        return paymentOptional.get();
    }

    @Override
    public PayrollPayment addParameter(PayrollPayment payrollPayment) {
        return iPayrollPaymentRepo.save(payrollPayment);
    }
    @Override
    public boolean updatePayrollPayment(PayrollPayment payrollPayment) {
        try {
            // Buscar el Pago en la base de datos por id
            Optional<PayrollPayment> parametersBd = iPayrollPaymentRepo.findById(payrollPayment.getId());

            // Si el pago existe, actualizar sus campos
            if (parametersBd.isPresent()) {
                PayrollPayment existingPayment = parametersBd.get();

                // Actualizar los campos del pago existente con los valores del pago proporcionado
                existingPayment.setDateInit(payrollPayment.getDateInit());
                existingPayment.setDateEnd(payrollPayment.getDateEnd());
                existingPayment.setEmployee(payrollPayment.getEmployee());
                existingPayment.setPaymentStatus(payrollPayment.isPaymentStatus());
                existingPayment.setTypePayroll(payrollPayment.getTypePayroll());
                existingPayment.setAccumulatedAmount(payrollPayment.getAccumulatedAmount());
                existingPayment.setTotal_accumulatedAmount(payrollPayment.getTotal_accumulatedAmount());
                existingPayment.setTotal_SecureSocial(payrollPayment.getTotal_SecureSocial());
                existingPayment.setTotal_numberOvertime(payrollPayment.getTotal_Overtime());
                existingPayment.setTotal_numberFestiveHours(payrollPayment.getTotal_numberFestiveHours());
                existingPayment.setTotal_Overtime(payrollPayment.getTotal_Overtime());
                existingPayment.setTotal_FestiveHours(payrollPayment.getTotal_FestiveHours());  // 12

                // Guardar la entidad actualizada en la base de datos
                iPayrollPaymentRepo.save(existingPayment);

                return true;
            }

            // Si no se encuentra el pago, devolver false
            return false;
        } catch (Exception e) {
            // Manejo de excepciones: Registrar o manejar la excepción según sea necesario
            System.err.println("Error updating payroll payment: " + e.getMessage());
            return false;
        }
    }



}


