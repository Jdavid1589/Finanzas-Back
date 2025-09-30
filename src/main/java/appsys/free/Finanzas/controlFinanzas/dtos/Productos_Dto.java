package appsys.free.Finanzas.controlFinanzas.dtos;


import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class Productos_Dto implements Serializable {

    private Long id;
    private String nombreProducto;
    private BigDecimal precioEstimado;
    private CategoriaGastos categoria;



}


