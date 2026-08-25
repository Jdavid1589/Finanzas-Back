package appsys.free.Agrocore.controlFinanzas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "nomina")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"cosecha", "empleado"})
public class Nomina {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "empleado_id")
    private Empleado empleado;

    //***************************//

    @Column(name = "anio", nullable = false)
    private Integer anio;

    @Column(name = "dias", nullable = false)
    private Integer dias;

    @Column(name = "semana", nullable = false)
    private Integer semana;

    //***************************//

    @Column(name = "cantidad_tipo_granel", nullable = false, precision = 7, scale = 2)
    private BigDecimal cantidadTipoGranel;

    @Column(name = "horas", nullable = false, precision = 7, scale = 2)
    private BigDecimal horas;

    @Column(name = "valor_dia", nullable = false)
    private Integer valorDia;

    @Column(name = "valor_hora", nullable = false)
    private Integer valorHora;

    @Column(name = "valor_semana", nullable = false)
    private Integer valorSemana;

    @Column(name = "valor_tipo_granel", nullable = false)
    private Integer valorTipoGranel;

    /*TOTALES NOMINA*/
    @Column(name = "total_nomina", nullable = false)
    private Integer totalNomina;

    @Column(name = "total_nomina_dias", nullable = false)
    private Integer totalNominaDias;

    @Column(name = "total_nomina_hora", nullable = false)
    private Integer totalNominaHora;

    @Column(name = "total_nomina_tipo_granel", nullable = false)
    private Integer totalNominaTipoGranel;


    // RELACION CON LA FINCA - LOTE O COSECHA
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cosecha_id")
    private Cosecha cosecha;

    // ESTADO DEL PAGO DE LA NOMINA
    @Column(name = "estado_pago", nullable = false)
    private Boolean estadoPago;

    // Valor minuto, se usa para la calculadora
    @Column(name = "minutos", nullable = false)
    private Integer minutos;


}
