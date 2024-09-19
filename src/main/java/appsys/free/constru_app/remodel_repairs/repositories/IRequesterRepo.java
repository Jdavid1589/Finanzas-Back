package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.StatusRequ;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface IRequesterRepo extends JpaRepository<Requester,Integer> {


    Optional<List<Requester>> findByStatusRequ(StatusRequ statusRequ);
}
