package appsys.free.Finanzas.controlFinanzas.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
    @Entity
    public class Ingresos {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Temporal(TemporalType.DATE)
        @Column(name = "fecha")
        private Date fechaIngreso;

        // Muchos ingresos pueden pertenecer a una sola categoría
        @ManyToOne
        @JoinColumn(name = "categIngresos")
        private CategoriaIngresos categIngresos;

        private String descripcion;

        @Column(name = "cantidad", precision = 10, scale = 2)
        private BigDecimal cantidad;




    /*Constructor*/
    public Ingresos(Long id) {
        this.id = id;
    }
}
