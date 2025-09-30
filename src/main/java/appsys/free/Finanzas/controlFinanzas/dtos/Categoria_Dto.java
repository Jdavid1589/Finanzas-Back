package appsys.free.Finanzas.controlFinanzas.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Categoria_Dto {

    private Long id;
    private String nombreCategoria;


}


