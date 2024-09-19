package appsys.free.constru_app.remodel_repairs.dtos;

import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.Transport;
import appsys.free.constru_app.remodel_repairs.entities.Work;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SeeRequester_dto {
    private int id;
    private String date;
    private String dateLimit;
    private String names;
    private String surNames;
    private String noDocument;
    private String phone;
    private String municipality;
    private String address;
    private int costWork;
    private int costEquipment;
    private int costMater;
    private int costTransp;
    private int costRequest;
    private List<Work_Dto>works;
    private List<Equipment>equipments;
    private List<Transport>transports;

    public SeeRequester_dto(int id, String date, String names,String surNames, String noDocument, String phone, String municipality, String address, int costWork, int costEquipment, int costMater, int costTransp, int costRequest) {
        this.id = id;
        this.date = date;
        this.names = names;
        this.surNames=surNames;
        this.noDocument = noDocument;
        this.phone = phone;
        this.municipality = municipality;
        this.address = address;
        this.costWork = costWork;
        this.costEquipment = costEquipment;
        this.costMater = costMater;
        this.costTransp = costTransp;
        this.costRequest = costRequest;
    }
}
