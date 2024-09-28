package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.dtos.Requester_Dto;
import appsys.free.constru_app.remodel_repairs.dtos.SeeRequester_dto;
import appsys.free.constru_app.remodel_repairs.entities.Customer;
import appsys.free.constru_app.remodel_repairs.entities.Equipment;
import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.Transport;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

public interface IRequesterService {

    boolean addRequester(HttpServletResponse reponse, Requester_Dto requesterDto);

    List<SeeRequester_dto> getRequests();

    boolean deleteRequest(int idRequest);

    Customer validNoDoc(String noDoc);

    int addUpdateEquipment(Equipment equipment,int idRequest);

    boolean deleteUpdateEquipment(int idEquip);

    boolean updateEquipment(Equipment equipment);

    int addUpdateTransport(Transport transport,int idRequest);

    boolean deleteUpdateTransport(int idTransport);

    boolean updateTransport(Transport transport);

    boolean startWorks(int idRequester);

    void reprintRequester(HttpServletResponse reponse, int idRequ);

    Requester_Dto getRequestById(int idRequest);

    Requester_Dto updateRequest(Requester_Dto requesterDto);



}
