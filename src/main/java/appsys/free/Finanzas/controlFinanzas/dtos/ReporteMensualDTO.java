package appsys.free.Finanzas.controlFinanzas.dtos;

import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ReporteMensualDTO implements Serializable {

    private String periodo;
    private List<CategoriaDetalleDTO> ingresos;
    private List<CategoriaDetalleDTO> gastos;
    private List<CategoriaDetalleDTO> presupuestos;
    private BigDecimal totalIngresos;
    private BigDecimal totalGastos;
    private BigDecimal totalPresupuestado;
    private BigDecimal balance;
    private BigDecimal desviacionPresupuesto;

}


