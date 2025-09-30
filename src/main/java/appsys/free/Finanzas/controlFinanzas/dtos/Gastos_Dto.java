package appsys.free.Finanzas.controlFinanzas.dtos;


import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import appsys.free.Finanzas.controlFinanzas.entities.Presupuesto;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Gastos_Dto implements Serializable {

    private Long id;
    private Date fechaGasto;
    private  BigDecimal montoGasto;
    private CategoriaGastos categoriaGastos;

   // private Long categoriaGastosId; // Solo el ID,






}


