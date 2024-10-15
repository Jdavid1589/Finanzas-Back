package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true,nullable = false)
    private String documentNumber;
    @Column(nullable = false)
    private String names;
    @Column(nullable = false)
    private String surnames;
    private String phoneNumber;
    private String address;
    private String email;
    @ManyToOne
    private Municipality municipality;


}
