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
public class SaldoPresupuestoDTO {

    private String periodo;
    private String categoria;
    private BigDecimal presupuesto;
    private BigDecimal gasto;
    private BigDecimal saldo;


    public SaldoPresupuestoDTO(String periodo,
                               String categoria,
                               BigDecimal presupuesto,
                               BigDecimal saldo) {
        this.periodo = periodo;
        this.categoria = categoria;
        this.presupuesto = presupuesto;
        this.gasto = BigDecimal.ZERO;
        this.saldo = saldo;
    }

}


