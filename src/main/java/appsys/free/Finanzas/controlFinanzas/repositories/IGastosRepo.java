package appsys.free.Finanzas.controlFinanzas.repositories;

import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IGastosRepo extends JpaRepository<Gastos, Long> {


    @Query("SELECT YEAR(g.fechaGasto), MONTH(g.fechaGasto), c.nombreCategoria, SUM(g.montoGasto) " +
            "FROM Gastos g " +
            "JOIN g.categGastos c " + // Debe coincidir con el nombre de la propiedad
            "GROUP BY YEAR(g.fechaGasto), MONTH(g.fechaGasto), c.nombreCategoria " +
            "ORDER BY YEAR(g.fechaGasto) DESC, MONTH(g.fechaGasto) DESC")
    List<Object[]> getGastosPorMesYCategoria2();
/*-----------------------------------------------------------*/


    @Query("SELECT c.nombreCategoria, SUM(g.montoGasto) " +
            "FROM Gastos g " +
            "JOIN g.categGastos c " +
            "WHERE YEAR(g.fechaGasto) = :anio AND MONTH(g.fechaGasto) = :mes " +
            "GROUP BY c.nombreCategoria")
    List<Object[]> getGastosPorCategoria(@Param("anio") int anio, @Param("mes") int mes);
/*--------------------------------------------------------*/
    // 🔹 Total de gastos por mes
    @Query("""
        SELECT
            FUNCTION('YEAR', g.fechaGasto) AS anio,
            FUNCTION('MONTH', g.fechaGasto) AS mes,
            COALESCE(SUM(g.montoGasto), 0) AS totalGastos
        FROM Gastos g
        GROUP BY FUNCTION('YEAR', g.fechaGasto), FUNCTION('MONTH', g.fechaGasto)
        ORDER BY anio DESC, mes DESC
    """)
    List<Object[]> getTotalGastosPorMes();

    // 🔹 Gastos agrupados por categoría
    @Query("""
        SELECT
            g.categGastos.nombreCategoria,
            SUM(g.montoGasto)
        FROM Gastos g
        GROUP BY g.categGastos.nombreCategoria
        ORDER BY SUM(g.montoGasto) DESC
    """)
    List<Object[]> getGastosPorCategoria();

    // 🔹 Gastos por mes y categoría
    @Query("""
        SELECT
            FUNCTION('YEAR', g.fechaGasto) AS anio,
            FUNCTION('MONTH', g.fechaGasto) AS mes,
            g.categGastos.nombreCategoria,
            SUM(g.montoGasto)
        FROM Gastos g
        GROUP BY FUNCTION('YEAR', g.fechaGasto), FUNCTION('MONTH', g.fechaGasto), g.categGastos.nombreCategoria
        ORDER BY anio DESC, mes DESC
    """)
    List<Object[]> getGastosPorMesYCategoria();


    // Total de gastos por mes
   /* @Query("SELECT YEAR(g.fechaGasto) AS anio, MONTH(g.fechaGasto) AS mes, SUM(g.montoGasto) " +
            "FROM Gastos g " +
            "GROUP BY YEAR(g.fechaGasto), MONTH(g.fechaGasto) " +
            "ORDER BY anio, mes")
    List<Object[]> getTotalGastosPorMes();
*/

    // Gastos agrupados por categoría

 /*@Query("SELECT g.categGastos.nombreCategoria, SUM( g.montoGasto) " +
            "FROM Gastos g " +
            "GROUP BY g.categGastos.nombreCategoria " +
            "ORDER BY SUM( g.montoGasto) DESC")
    List<Object[]> getGastosPorCategoria();
*/

  /*  @Query("SELECT YEAR(g.fechaGasto) AS anio, MONTH(g.fechaGasto) AS mes, g.categGastos.nombreCategoria, SUM(montoGasto) " +
            "FROM Gastos g " +
            "GROUP BY YEAR(g.fechaGasto), MONTH(g.fechaGasto), g.categGastos.nombreCategoria " +
            "ORDER BY anio, mes")
    List<Object[]> getGastosPorMesYCategoria();*/












}
