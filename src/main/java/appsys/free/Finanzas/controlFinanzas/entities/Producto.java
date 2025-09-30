package appsys.free.Finanzas.controlFinanzas.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombreProducto;

    private BigDecimal precioEstimado;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private CategoriaGastos categoria;


    public Producto(Long id) {
        this.id = id;
    }


}
