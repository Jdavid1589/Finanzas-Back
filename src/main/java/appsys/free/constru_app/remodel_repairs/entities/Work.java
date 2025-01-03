package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "works")
public class Work {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Requester requester;
    private String description;
    private int costManp;
    private int numberManp;
    private String typeManp;
    private int laborCost;
    private int materialCost;

    private String addres;

    public Work(Long id) {
        this.id = id;
    }
}
