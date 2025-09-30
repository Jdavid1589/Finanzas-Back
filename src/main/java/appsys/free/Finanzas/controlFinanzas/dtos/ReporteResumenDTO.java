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
public class ReporteResumenDTO implements Serializable {

    private BigDecimal totalGeneralIngresos;
    private BigDecimal totalGeneralPresupuesto;
    private BigDecimal totalGeneralGastos;
    private BigDecimal balanceGeneral;
    private List<ReporteFinancieroMensualDTO> detalleMensual;

}


