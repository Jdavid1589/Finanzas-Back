package appsys.free.Finanzas.controlFinanzas.repositories;

import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IIngresoRepo extends JpaRepository<Ingresos, Long> {

    @Query("SELECT YEAR(i.fechaIngreso), MONTH(i.fechaIngreso), c.nombreCategoria, SUM(i.cantidad) " +
            "FROM Ingresos i " +
            "JOIN i.categIngresos c " + // Debe coincidir con el nombre de la propiedad
            "GROUP BY YEAR(i.fechaIngreso), MONTH(i.fechaIngreso), c.nombreCategoria " +
            "ORDER BY YEAR(i.fechaIngreso) DESC, MONTH(i.fechaIngreso) DESC")
    List<Object[]> getIngresosPorMesYCategoria();

/*---------------------------------------------------------------*/
    @Query("SELECT c.nombreCategoria, SUM(i.cantidad) " +
            "FROM Ingresos i " +
            "JOIN i.categIngresos c " +
            "WHERE YEAR(i.fechaIngreso) = :anio AND MONTH(i.fechaIngreso) = :mes " +
            "GROUP BY c.nombreCategoria")
    List<Object[]> getIngresosPorCategoria(@Param("anio") int anio, @Param("mes") int mes);


    /*---------------------------------------------------*/
    //Ultima Consulta para reportes

    @Query("SELECT YEAR(i.fechaIngreso), MONTH(i.fechaIngreso), c.nombreCategoria, SUM(i.cantidad) " +
            "FROM Ingresos i " +  // Cambiado de "Ingreso" a "Ingresos"
            "JOIN i.categIngresos c " +  // Usando el alias correcto
            "GROUP BY YEAR(i.fechaIngreso), MONTH(i.fechaIngreso), c.nombreCategoria " +
            "ORDER BY YEAR(i.fechaIngreso) DESC, MONTH(i.fechaIngreso) DESC, c.nombreCategoria")
    List<Object[]> getTotalIngresosPorMesYCategoria();



    // Total de ingresos por mes
     @Query("SELECT YEAR(i.fechaIngreso) AS anio, MONTH(i.fechaIngreso) AS mes, SUM(i.cantidad) " +
            "FROM Ingresos i " +
            "GROUP BY YEAR(i.fechaIngreso), MONTH(i.fechaIngreso) " +
            "ORDER BY anio, mes")
    List<Object[]> getTotalIngresosPorMes();

    // Ingresos agrupados por categoría
    @Query("SELECT i.categIngresos.nombreCategoria, SUM(i.cantidad) " +
            "FROM Ingresos i " +
            "GROUP BY i.categIngresos.nombreCategoria " +
            "ORDER BY SUM(i.cantidad) DESC")
    List<Object[]> getIngresosPorCategoria();

    // Buscar todos los ingresos por el id de la categoría
    List<Ingresos> findByCategIngresos_Id(Long categoriaId);

    // Buscar todos los ingresos por nombre de categoría
    List<Ingresos> findByCategIngresos_NombreCategoria(String nombreCategoria);

}
