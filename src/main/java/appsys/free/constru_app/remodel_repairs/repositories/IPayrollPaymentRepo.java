package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Employee;
import appsys.free.constru_app.remodel_repairs.entities.PayrollPayment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;



import java.util.Optional;

public interface IPayrollPaymentRepo extends JpaRepository<PayrollPayment,Integer> {

    Optional<PayrollPayment> findByEmployee(Employee employee);

    @Query("SELECT p FROM PayrollPayment p WHERE p.employee.id = :idEmployed ORDER BY p.id DESC")
    Page<PayrollPayment> findByEmployeeIdOrderByIdDesc(int idEmployed, Pageable pageable);



}
