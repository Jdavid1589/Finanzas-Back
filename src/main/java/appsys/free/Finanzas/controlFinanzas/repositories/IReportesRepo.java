package appsys.free.Finanzas.controlFinanzas.repositories;

import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface IReportesRepo extends JpaRepository<Gastos, Long> {

    @Query("SELECT new appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO(" +
            "CONCAT(YEAR(p.fecha), '-', LPAD(MONTH(p.fecha), 2, '0')), " +  // Periodo YYYY-MM
            "c.nombreCategoria, " +                                         // Nombre de la categoría
            "COALESCE(SUM(p.monto), 0), " +                                // Total del presupuesto
            "COALESCE(SUM(g.montoGasto), 0), " +                           // Total de los gastos
            "COALESCE(SUM(p.monto), 0) - COALESCE(SUM(g.montoGasto), 0)) " + // Saldo (presupuesto - gasto)
            "FROM Presupuesto p " +                // 👈 Se toma la entidad Presupuesto como base
            "JOIN p.categoriaGastos c " +          // 👈 Relación ManyToOne hacia CategoriaGastos
            "LEFT JOIN Gastos g " +                // 👈 Relación hacia gastos, para ver si hubo consumo
            "ON g.categGastos = c " +              // 👈 El gasto pertenece a la misma categoría
            "AND YEAR(g.fechaGasto) = YEAR(p.fecha) " +  // 👈 Mismo año
            "AND MONTH(g.fechaGasto) = MONTH(p.fecha) " + // 👈 Mismo mes
            "GROUP BY YEAR(p.fecha), MONTH(p.fecha), c.nombreCategoria " +
            "ORDER BY YEAR(p.fecha), MONTH(p.fecha)")
    List<SaldoPresupuestoDTO> obtenerSaldoPresupuesto();


}
