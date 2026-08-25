package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "ventas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"cosecha", "cliente"})
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    //CANTIDAD PRODUCTO VENDIDO (MOÑAS - KG - SACOS)
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "valor_unidad", nullable = false)
    private Integer valorUnidad;

    @Column(name = "valor_total", nullable = false)
    private Integer valorTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosecha_id")
    private Cosecha cosecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;
}
