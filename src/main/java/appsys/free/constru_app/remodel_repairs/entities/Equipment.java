package appsys.free.constru_app.remodel_repairs.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "equipments")
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Requester requester;

    private String equipment;
    private int numberEquipments;
    private int dayValue;
    private int numberDays;
    private int subtDayValue;
    private int hourValue;
    private int numberHours;
    private int subtHourValue;
    private int totalEquipment;



}
