package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;

import java.util.List;

public interface IMaterialRepo extends JpaRepository<Material,Long> {
    List<Material>findByWork(Work work);


    @Modifying
    void deleteByWork(Work work);
}
