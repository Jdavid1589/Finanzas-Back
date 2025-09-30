package appsys.free.Finanzas.controlFinanzas.entities;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "presupuesto")
public class Presupuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date fecha;   // Fecha de asignación del presupuesto

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal monto;   // Monto presupuestado

    @Column(nullable = false, length = 255)
    private String descripcion; // Descripción (ej. Presupuesto Enero, Proyecto X)

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private CategoriaGastos categoriaGastos; // Relación con categoría

    /*Constructor*/
    public Presupuesto(Long id) {
        this.id = id;
    }
}
