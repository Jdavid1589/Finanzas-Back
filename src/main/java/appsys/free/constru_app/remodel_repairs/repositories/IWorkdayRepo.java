package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Workday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IWorkdayRepo extends JpaRepository<Workday,Integer> {

   /* @Query("SELECT p.weeklyHours FROM ParametersWorkday p WHERE p.id = :id")
    Optional<Integer> findWeeklyHoursById(@Param("id") Long id);
*/
}
