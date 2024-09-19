package appsys.free.constru_app.remodel_repairs.repositories;

import appsys.free.constru_app.remodel_repairs.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ICustomerRepo extends JpaRepository<Customer,Integer> {

    Optional<Customer> findByDocumentNumber(String documentNumber);
}
