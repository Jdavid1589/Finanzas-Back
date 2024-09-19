package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IEquipmentRepo extends JpaRepository<Equipment,Integer> {
    List<Equipment> findByRequester(Requester requester);

    @Query(value = "select sum(total_equipment) from equipments where requester_id = ?1",nativeQuery = true)
    Integer getTotalEquipmentByRequester(int idRequester);
}
