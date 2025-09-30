package appsys.free.Finanzas.controlFinanzas.repositories;

import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import appsys.free.Finanzas.controlFinanzas.entities.Presupuesto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPresupuestoRepo extends JpaRepository<Presupuesto, Long> {

    /*---------------------------Clase Reportes-Financieros---------------------------------*/
    // 🔹 Presupuestos por Mes y categoría (Reportes-Financieros)
    @Query("SELECT YEAR(p.fecha), MONTH(p.fecha), c.nombreCategoria, SUM(p.monto) " +
            "FROM Presupuesto p " +
            "JOIN p.categoriaGastos c " +
            "GROUP BY YEAR(p.fecha), MONTH(p.fecha), c.nombreCategoria " +
            "ORDER BY YEAR(p.fecha) DESC, MONTH(p.fecha) DESC")
    List<Object[]> getPresupuestoPorMesYCategoria();

    @Query("SELECT p FROM Presupuesto p WHERE YEAR(p.fecha) = :anio AND MONTH(p.fecha) = :mes")
    List<Presupuesto> findByAnioAndMes(@Param("anio") int anio, @Param("mes") int mes);


    ///------------------------///---------------------------///------------------///

    /*---------------------------Clase Presupuestos---------------------------------*/
    // 🔹 Presupuestos por categoría
    @Query("SELECT c.nombreCategoria, SUM(p.monto) " +
            "FROM Presupuesto p " +
            "JOIN p.categoriaGastos c " +
            "WHERE YEAR(p.fecha) = :anio AND MONTH(p.fecha) = :mes " +
            "GROUP BY c.nombreCategoria")
    List<Object[]> getPresupuestoPorCategoria(@Param("anio") int anio, @Param("mes") int mes);

    /*--------------------------------------------------------------*/

      // 🔹 Presupuestos por categoría (con gastos)
      @Query("""
    SELECT new appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO(
        CONCAT(FUNCTION('YEAR', p.fecha), '-', LPAD(FUNCTION('MONTH', p.fecha), 2, '0')),
        p.categoriaGastos.nombreCategoria,
        COALESCE(SUM(p.monto), 0),
        COALESCE(SUM(g.montoGasto), 0),
        COALESCE(SUM(p.monto), 0) - COALESCE(SUM(g.montoGasto), 0)
    )
    FROM Presupuesto p
    LEFT JOIN Gastos g
        ON g.categGastos.id = p.categoriaGastos.id
       AND FUNCTION('YEAR', g.fechaGasto) = FUNCTION('YEAR', p.fecha)
       AND FUNCTION('MONTH', g.fechaGasto) = FUNCTION('MONTH', p.fecha)
    GROUP BY FUNCTION('YEAR', p.fecha), FUNCTION('MONTH', p.fecha), p.categoriaGastos.nombreCategoria
    ORDER BY FUNCTION('YEAR', p.fecha) DESC, FUNCTION('MONTH', p.fecha) DESC, p.categoriaGastos.nombreCategoria
""")
      List<SaldoPresupuestoDTO> getPresupuestoPorCategoria();

    /*--------------------------------------------------------------*/
      // 🔹 Presupuestos por mes
        @Query("""
        SELECT new appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO(
            CONCAT(FUNCTION('YEAR', p.fecha), '-', LPAD(FUNCTION('MONTH', p.fecha), 2, '0')),
            'TOTAL',
            COALESCE(SUM(p.monto), 0),
            (
                SELECT COALESCE(SUM(g.montoGasto), 0)
                FROM Gastos g
                WHERE FUNCTION('YEAR', g.fechaGasto) = FUNCTION('YEAR', p.fecha)
                  AND FUNCTION('MONTH', g.fechaGasto) = FUNCTION('MONTH', p.fecha)
            ),
            COALESCE(SUM(p.monto), 0) -
            (
                SELECT COALESCE(SUM(g.montoGasto), 0)
                FROM Gastos g
                WHERE FUNCTION('YEAR', g.fechaGasto) = FUNCTION('YEAR', p.fecha)
                  AND FUNCTION('MONTH', g.fechaGasto) = FUNCTION('MONTH', p.fecha)
            )
        )
        FROM Presupuesto p
        GROUP BY FUNCTION('YEAR', p.fecha), FUNCTION('MONTH', p.fecha)
        ORDER BY FUNCTION('YEAR', p.fecha) DESC, FUNCTION('MONTH', p.fecha) DESC
    """)
        List<SaldoPresupuestoDTO> getPresupuestoPorMes();

    /*--------------------------------------------------------------*/

    // 🔹 Presupuesto por mes y categoría
    @Query("""
    SELECT new appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO(
        CONCAT(FUNCTION('YEAR', p.fecha), '-', LPAD(FUNCTION('MONTH', p.fecha), 2, '0')),
        p.categoriaGastos.nombreCategoria,
        COALESCE(SUM(p.monto), 0),
        COALESCE(SUM(g.montoGasto), 0),
        COALESCE(SUM(p.monto), 0) - COALESCE(SUM(g.montoGasto), 0)
    )
    FROM Presupuesto p
    LEFT JOIN Gastos g
        ON g.categGastos.id = p.categoriaGastos.id
       AND FUNCTION('YEAR', g.fechaGasto) = FUNCTION('YEAR', p.fecha)
       AND FUNCTION('MONTH', g.fechaGasto) = FUNCTION('MONTH', p.fecha)
    GROUP BY FUNCTION('YEAR', p.fecha), FUNCTION('MONTH', p.fecha), p.categoriaGastos.nombreCategoria
    ORDER BY FUNCTION('YEAR', p.fecha) DESC, FUNCTION('MONTH', p.fecha) DESC, p.categoriaGastos.nombreCategoria
""")
    List<SaldoPresupuestoDTO> getPresupuestoPorMesYCategoria2();

    /*--------------------------------------------------*/


}
