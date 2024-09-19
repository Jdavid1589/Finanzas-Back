package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "requesters")
public class Requester {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date dateLimit;
    @ManyToOne
    private Customer customer;
    @ManyToOne
    private StatusRequ statusRequ;
    @ManyToOne
    private Municipality municipality;
    private String address;
    private int totalWork;
    private int totalMater;
    private int totalEquipment;
    private int totalTransport;
    private int totalValueRequest;
    @Temporal(TemporalType.DATE)
    private Date dateInit;

    public Requester(int id) {
        this.id = id;
    }
}
