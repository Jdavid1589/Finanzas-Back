package appsys.free.Finanzas.controlFinanzas.dtos;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import appsys.free.Finanzas.controlFinanzas.entities.CategoriaIngresos;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PresupuestoDto implements Serializable {


    private Long id;
    private Date fecha;
    private BigDecimal monto;
    private String descripcion;
    private CategoriaGastos categoriaGastos;


}


