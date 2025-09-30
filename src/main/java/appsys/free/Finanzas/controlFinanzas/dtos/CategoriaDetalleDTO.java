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
public class CategoriaDetalleDTO implements Serializable {

    private String tipo; // "INGRESO", "GASTO", "PRESUPUESTO"
    private String categoria;
    private BigDecimal monto;
    private BigDecimal presupuestado; // solo para gastos
    private BigDecimal diferencia; // presupuestado - monto (solo para gastos)

}


