package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.dtos.PayrollValidationDto;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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


    @Override
    public PayrollValidationDto validNewPayroll(int idEmployed, boolean paymentStatus) {
        // Crear un objeto Pageable para obtener solo el primer elemento (página 0, tamaño 1)
        Pageable pageable = PageRequest.of(0, 1);

        // Obtener una página de PayrollPayment ordenada por id en orden descendente para el empleado dado
        Page<PayrollPayment> payrollPayments = iPayrollPaymentRepo.findByEmployeeIdOrderByIdDesc(idEmployed, pageable);

        // Verificar si la página contiene algún elemento
        if (payrollPayments.hasContent()) {
            // Obtener el primer PayrollPayment de la lista
            PayrollPayment payrollPayment = payrollPayments.getContent().get(0);

            // Obtener el estado de pago actual del PayrollPayment
            boolean currentPaymentStatus = payrollPayment.isPaymentStatus();

            // Obtener el id del tipo de nómina del PayrollPayment
            int typePayrollId = payrollPayment.getTypePayroll().getId();

            // Devolver un nuevo objeto PayrollValidationDto con el estado de pago actual y el id del tipo de nómina
            return new PayrollValidationDto(currentPaymentStatus, typePayrollId);
        }

        // Si no se encuentra ningún PayrollPayment, devolver un DTO con valores predeterminados
        return new PayrollValidationDto(true, 0);
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


