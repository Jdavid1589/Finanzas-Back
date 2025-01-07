package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Data
@Entity
@NoArgsConstructor
@Table(name = "TypesPayrolls")
public class TypePayroll implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String namePayroll;


    public TypePayroll(int id) {
        this.id = id;
    }


}
