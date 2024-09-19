package appsys.free.constru_app.remodel_repairs.dtos;

import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import appsys.free.constru_app.remodel_repairs.entities.Work;
import lombok.Data;

import java.util.List;

@Data
public class Requester_Dto {
    private int id;
    private String numberDoc;
    private String names;
    private String surnames;
    private String phoneNumber;
    private String address;
    private int departament;

    private int idMunicipality;
    private Municipality municipalityObj;
    private String email;
    private String dateLimit;
    private List<Work_Dto> listWorksToBeDone;
    private int subtWorks;
    private List<Equipment>listEquipment;
    private List<Transport>listTransport;
    private int totalRequest;
}
