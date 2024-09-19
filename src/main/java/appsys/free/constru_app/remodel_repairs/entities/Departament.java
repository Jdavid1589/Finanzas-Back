package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@Table(name = "departaments")
public class Departament implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;

    public Departament() {
    }

    public Departament(int id) {
        this.id = id;
    }
}
