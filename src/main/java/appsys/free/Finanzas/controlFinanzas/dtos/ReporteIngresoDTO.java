package appsys.free.Finanzas.controlFinanzas.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReporteIngresoDTO {

    private String periodo;
    private String categoria;
    private BigDecimal ingreso;
    private BigDecimal totalMes; // Total del mes
    private BigDecimal totalGeneral; // Total general acumulado

}


