package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Employee;
import appsys.free.constru_app.remodel_repairs.entities.PayrollPayment;
import appsys.free.constru_app.remodel_repairs.entities.TypePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IPayrollPaymentRepo extends JpaRepository<PayrollPayment,Integer> {


   // List<PayrollPayment> findByEnable(boolean paymentStatus);


    //Optional<PayrollPayment> findByEmployeeAndPaymentStatus(Employee employee, boolean paymentStatus);
    Optional<PayrollPayment> findByEmployee(Employee employee);



}
