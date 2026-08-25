package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "insumos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "gastos")
public class Insumo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // En el dump original la columna se llama "insumo" (es la descripcion/nombre del insumo)
    @Column(name = "insumo")
    private String nombre;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "costo", nullable = false)
    private Integer costo;

    @OneToMany(mappedBy = "insumo", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Gasto> gastos = new ArrayList<>();
}
