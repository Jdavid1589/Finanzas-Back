package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "payrolls_history")
public class PayrollHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

  //  @Column(precision = 10, scale = 2, nullable = false) // Cantidad Acumulada
    // private BigDecimal accumulatedAmount;
    private int amountTypePayroll; //Cantidad Tipo Nomina

    private int numberOvertime;          // Cantidad Horas Extras
    private int numberFestiveHours;     //  Cantidad Horas Festivas


    @ManyToOne
   @JoinColumn(name = "payrollPayment_id") // Esto define la clave foránea
    private PayrollPayment payrollPayment;







}
