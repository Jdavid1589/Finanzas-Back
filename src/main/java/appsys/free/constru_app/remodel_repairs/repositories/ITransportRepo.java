package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ITransportRepo extends JpaRepository<Transport,Integer> {
    List<Transport> findByRequester(Requester requester);
}
