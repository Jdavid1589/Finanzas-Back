package appsys.free.Agrocore.controlFinanzas.repositories;

import appsys.free.Agrocore.controlFinanzas.entities.Gasto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface GastoRepository extends JpaRepository<Gasto, Long> {

    List<Gasto> findByCosechaId(Integer cosechaId);

    List<Gasto> findByTipoGastoId(Integer tipoGastoId);

    List<Gasto> findByInsumoId(Integer insumoId);

    List<Gasto> findByFechaBetween(LocalDate desde, LocalDate hasta);
}
