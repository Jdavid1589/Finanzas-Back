package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payrolls_payment")
public class PayrollPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date dateInit;

    @Temporal(TemporalType.DATE)
    private Date dateEnd;

    private boolean paymentStatus;       // Estado de Pago

    // @Column(precision = 10, scale = 2, nullable = false)  Cantidad Acumulada
    // private BigDecimal accumulatedAmount;
    private int accumulatedAmount;

    private int unitValue;
    private int subTotal;
    private int subTotalSecureSocial;
    private int numberOvertime;          // Cantidad Horas Extras
    private int numberFestiveHours;     //  Cantidad Horas Festivas
    private int valueOvertime;         //   Valor Hora Extra
    private int valueFestiveHour ;    //    Valor Hora Festiva

    @ManyToOne
    @JoinColumn(name = "typePayroll_id") // Esto define la clave foránea
    private TypePayroll typePayroll;

    @ManyToOne
    @JoinColumn(name = "employee_id") // Esto define la clave foránea
    private Employee employee;





}
