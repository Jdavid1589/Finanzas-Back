package appsys.free.Agrocore.controlFinanzas.repositories;

import appsys.free.Agrocore.controlFinanzas.entities.Cosecha;
import appsys.free.Agrocore.controlFinanzas.entities.EstadoCosecha;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CosechaRepository extends JpaRepository<Cosecha, Integer> {

    List<Cosecha> findByEstado(EstadoCosecha estado);

    List<Cosecha> findByTipoCosechaId(Integer tipoCosechaId);

    List<Cosecha> findByTipoGranelId(Integer tipoGranelId);
}
