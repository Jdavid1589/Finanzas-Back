package appsys.free.constru_app.remodel_repairs.services.interfaces;


import appsys.free.constru_app.remodel_repairs.dtos.PayrollValidationDto;
import appsys.free.constru_app.remodel_repairs.entities.PayrollPayment;

import java.util.List;

public interface IPayrollPaymentService {

   boolean updatePayrollPayment(PayrollPayment payrollPayment);

    PayrollPayment addParameter(PayrollPayment payrollPayment);

    PayrollPayment getPaymentById(int id);

    List<PayrollPayment> getPayment();

    PayrollValidationDto validNewPayroll(int idEmployed, boolean paymentStatus);




}
