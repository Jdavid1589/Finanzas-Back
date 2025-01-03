package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.PayrollHistory;
import appsys.free.constru_app.remodel_repairs.entities.TypePayroll;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPayrollHistoryRepo extends JpaRepository<PayrollHistory,Integer> {

}
