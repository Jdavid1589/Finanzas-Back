package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cosechas")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"gastos", "nominas", "ventas"})
public class Cosecha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre")
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_cosecha_id")
    private TipoCosecha tipoCosecha;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tipo_granel_id")
    private TipoGranel tipoGranel;

    @Column(name = "fecha_final")
    private LocalDate fechaFinal;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "total_insumos", nullable = false)
    private Integer totalInsumos;

    @Column(name = "total_valor_fletes", nullable = false)
    private Integer totalValorFletes;

    @Column(name = "total_valor_nomina", nullable = false)
    private Integer totalValorNomina;

    @Column(name = "total_venta", nullable = false)
    private Integer totalVenta;

    @Column(name = "utilidad", nullable = false)
    private Integer utilidad;

    // Antes era un int crudo (1, 2...) sin significado explicito. Ahora es
    // un enum real -- @Enumerated(EnumType.STRING) guarda el NOMBRE del
    // enum ("ACTIVA") en la columna, no su posicion numerica (EnumType.ORDINAL
    // es fragil: si reordenas los valores del enum, los datos viejos
    // apuntarian al valor equivocado sin que nadie lo note).
    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 20)
    private EstadoCosecha estado;

    @Column(name = "total_otros", nullable = false)
    private Integer totalOtros;

    @Column(name = "total_produccion", nullable = false)
    private Double totalProduccion;

    @Column(name = "total_servicios", nullable = false)
    private Integer totalServicios;

    // Sin "cascade" a proposito (antes tenia CascadeType.ALL). Nunca lo
    // necesitamos para guardar -- Gasto/Nomina/Venta siempre se guardan
    // directo con su propio repository, nunca via cosecha.getGastos().add(...).
    // Con CascadeType.ALL, borrar una Cosecha borraba en cascada TODOS sus
    // gastos/nomina/ventas sin preguntar -- peligroso para datos financieros.
    // Ahora el borrado se bloquea explicitamente en CosechaServiceImpl.delete()
    // si existen registros asociados (ver ese metodo para el detalle).
    @OneToMany(mappedBy = "cosecha", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Gasto> gastos = new ArrayList<>();

    @OneToMany(mappedBy = "cosecha", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Nomina> nominas = new ArrayList<>();

    @OneToMany(mappedBy = "cosecha", fetch = FetchType.LAZY)
    @Builder.Default
    private List<Venta> ventas = new ArrayList<>();
}