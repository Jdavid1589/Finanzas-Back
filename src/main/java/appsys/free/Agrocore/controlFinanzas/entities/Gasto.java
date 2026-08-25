package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "gastos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"cosecha", "insumo", "tipoGasto"})
public class Gasto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha")
    private LocalDate fecha;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "costo_insumo", nullable = false)
    private Integer costoInsumo;

    @Column(name = "total", nullable = false)
    private Integer total;

    /* Columnas para separar el tipo de gasto y hacer los calculos totales por Categoria */
    @Column(name = "tipo_flete")
    private String tipoFlete;

    @Column(name = "tipo_servicio")
    private String tipoServicio;

    @Column(name = "detalle")
    private String detalle;

    /* Tablas relacionadas*/
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosecha_id")
    private Cosecha cosecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_gasto_id")
    private TipoGasto tipoGasto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "insumo_id")
    private Insumo insumo;


}