package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.Setter;
import net.bytebuddy.implementation.bind.MethodDelegationBinder;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String documentNumber;
    private String names;
    private String surnames;
    private String phoneNumber;
    private String address;
    private String email;
    @ManyToOne
    private Municipality municipality;


}
