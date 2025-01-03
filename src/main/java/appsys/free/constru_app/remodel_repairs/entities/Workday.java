package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "parameters_workday")
public class Workday {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int weeklyHours; // Horas Semanal



    /*Constructor */

    public Workday(int id) {
        this.id = id;
    }
}
