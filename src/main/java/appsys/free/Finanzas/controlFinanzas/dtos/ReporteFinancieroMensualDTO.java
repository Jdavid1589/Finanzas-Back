package appsys.free.Finanzas.controlFinanzas.dtos;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReporteFinancieroMensualDTO  implements Serializable {

    private String periodo; // Formato: "YYYY-MM"
    private List<CategoriaDetalleDTO> categorias;
    private BigDecimal totalIngresos;
    private BigDecimal totalPresupuesto;
    private BigDecimal totalGastos;
    private BigDecimal balance; // ingresos - gastos
    private BigDecimal desviacionPresupuesto; // presupuesto - gastos


}


