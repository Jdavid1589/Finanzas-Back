package appsys.free.Finanzas.controlFinanzas.dtos;

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
public class Ingreso_Dto implements Serializable {

    private Long id;
    private String descripcion;
    private BigDecimal cantidad;
    private Date fechaIngreso;
    private CategoriaIngresos categoriaIngresos;




}


