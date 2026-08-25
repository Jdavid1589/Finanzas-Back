package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipo_gasto")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "gastos")
public class TipoGasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo")
    private String tipo;

    @OneToMany(mappedBy = "tipoGasto", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Gasto> gastos = new ArrayList<>();
}
