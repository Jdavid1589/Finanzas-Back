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

    private int amountTypePayroll;      // Cantidad Tipo Nomina
    private int numberOvertime;         // Cantidad horas Extras
    private int numberFestiveHours;     // Cantidad Horas Festivas
    private int unitValueOrdinary;      // Valor Hora Ordinaria $
    private int unitValueOvertime;      // Valor Hora Extra $
    private int unitValueFestive;       // Valor Hora Festiva $
    private int subTotal_OrdinaryHours; // Sub Total Valor Hora Ordinaria $
    private int subTotal_FestiveHours;  // Sub Total Valor Hora Festiva $
    private int subTotal_OvertimeHours;  // Sub Total Valor Hora Extra  $


    @ManyToOne
   @JoinColumn(name = "payrollPayment_id") // Esto define la clave foránea
    private PayrollPayment payrollPayment;









}
