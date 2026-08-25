package appsys.free.Agrocore.controlFinanzas.repositories;

import appsys.free.Agrocore.controlFinanzas.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    Optional<Cliente> findByNit(String nit);
}
