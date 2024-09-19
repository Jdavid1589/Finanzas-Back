package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface IWorkRepo extends JpaRepository<Work,Long> {
    List<Work>findByRequester(Requester requester);


}
