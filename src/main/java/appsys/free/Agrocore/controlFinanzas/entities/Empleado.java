package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "empleado")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "nominas")
public class Empleado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "no_documento", nullable = false, unique = true, length = 100)
    private String noDocumento;

    @Column(name = "enable", nullable = false)
    private Boolean enable;

    @Column(name = "correo")
    private String correo;

    @Column(name = "telefonos")
    private String telefonos;

    @OneToMany(mappedBy = "empleado", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Nomina> nominas = new ArrayList<>();
}
