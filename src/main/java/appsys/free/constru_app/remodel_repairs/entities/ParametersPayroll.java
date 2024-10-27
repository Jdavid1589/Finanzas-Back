package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parameters_payroll")
public class ParametersPayroll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String profile;
    private int suggestedPayroll;
    private int socialSecurity;

    public ParametersPayroll(int id) {
        this.id = id;
    }





}
