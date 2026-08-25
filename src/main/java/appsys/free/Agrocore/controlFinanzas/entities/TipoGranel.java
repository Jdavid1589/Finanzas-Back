package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tipo_granel")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "cosechas")
public class TipoGranel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "tipo")
    private String tipo;

    @OneToMany(mappedBy = "tipoGranel", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Cosecha> cosechas = new ArrayList<>();
}
