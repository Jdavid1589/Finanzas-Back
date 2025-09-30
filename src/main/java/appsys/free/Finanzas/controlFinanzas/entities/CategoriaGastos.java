package appsys.free.Finanzas.controlFinanzas.entities;

import lombok.*;

import javax.persistence.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class CategoriaGastos {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String nombreCategoria;

    public CategoriaGastos(Long id) {
        this.id = id;
    }
}
