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

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne
    @JoinColumn(name = "typePayroll_id") // Esto define la clave foránea
    private TypePayroll typePayroll;

    @Temporal(TemporalType.DATE)
    private Date dateInit;
    @Temporal(TemporalType.DATE)
    private Date dateEnd;
    private boolean paymentStatus;           // Estado de Pago
    private int accumulatedAmount;          // Cantidad Acumulada  (nominas)
    private int total_accumulatedAmount;    // Total Costos Acumlados $
    private int total_SecureSocial;         // Total Costos Seguridad Social Acumlada $
    private int total_numberFestiveHours;   // Total Cantidad Horas Festivas
    private int total_numberOvertime;       // Total Cantidad Horas Extras
    private int total_Overtime;             // Total Costos Horas Extras
    private int total_FestiveHours;         // Total Costos Horas Festivas






}
