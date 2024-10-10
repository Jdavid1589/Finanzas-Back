package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.controllers.RequesterController;
import appsys.free.constru_app.remodel_repairs.dtos.Requester_Dto;
import appsys.free.constru_app.remodel_repairs.dtos.SeeRequester_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.*;
import appsys.free.constru_app.remodel_repairs.repositories.*;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IRequesterService;
import appsys.free.constru_app.remodel_repairs.util.PdfUtil;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class RequesterImplService implements IRequesterService {

    private static final Logger logger = LoggerFactory.getLogger(RequesterImplService.class);

    @Autowired
    IRequesterRepo iRequesterRepo;

    @Autowired
    ICustomerRepo iCustomerRepo;

    @Autowired
    IWorkRepo iWorkRepo;

    @Autowired
    IMunicipalityRepo iMunicipalityRepo;

    @Autowired
    IDepartamentRepo iDepartamentRepo;

    @Autowired
    IMaterialRepo iMaterialRepo;

    @Autowired
    IEquipmentRepo iEquipmentRepo;

    @Autowired
    ITransportRepo iTransportRepo;

    @Transactional
    @Override
    public boolean addRequester(HttpServletResponse response, Requester_Dto requesterDto) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Customer customer;
            Optional<Customer> resp = iCustomerRepo.findByDocumentNumber(requesterDto.getNumberDoc());
            if (resp.isPresent()) {
                customer = resp.get();
            } else {
                customer = new Customer();
                customer.setDocumentNumber(requesterDto.getNumberDoc());
            }

            customer.setNames(requesterDto.getNames());
            customer.setSurnames(requesterDto.getSurnames());
            customer.setPhoneNumber(requesterDto.getPhoneNumber());
            customer.setEmail(requesterDto.getEmail());
            customer.setAddress(requesterDto.getAddress());
            customer.setMunicipality(new Municipality(requesterDto.getIdMunicipality()));
            Customer customerDb = iCustomerRepo.save(customer);

            Requester requester = new Requester();

            requester.setDateLimit(formatter.parse(requesterDto.getDateLimit()));
            requester.setCustomer(customerDb);
            requester.setStatusRequ(new StatusRequ(2));

            Totals totals = getTotalRequest(requesterDto);
            requester.setTotalWork(totals.totalWork);
            requester.setTotalMater(totals.totalMater);
            requester.setTotalEquipment(totals.totalEquipment);
            requester.setTotalTransport(totals.totalTransport);
            requester.setTotalValueRequest(totals.totalRequest);

            requester.setDateInit(new Date());
            Requester requesterDb = iRequesterRepo.save(requester);
            requesterDto.setId(requesterDb.getId());
            requesterDto.setMunicipalityObj(getMunicipalityObj(requesterDto.getIdMunicipality()));
            if (!requesterDto.getListWorksToBeDone().isEmpty()) {
                if (requesterDto.getListWorksToBeDone().size() > 0) {
                    for (Work_Dto workDto : requesterDto.getListWorksToBeDone()) {
                        Work workIns = new Work();
                        workIns.setRequester(requesterDb);
                        workIns.setTypeManp(workDto.getTypeManp());
                        workIns.setNumberManp(workDto.getNumberManp());
                        workIns.setMaterialCost(workDto.getMaterialCost());
                        workIns.setLaborCost(workDto.getLaborCost());
                        workIns.setDescription(workDto.getDescription());
                        workIns.setCostManp(workDto.getCostManp());

                        workIns.setAddres(workDto.getAddress());
                        Work workDb = iWorkRepo.save(workIns);

                        if (!workDto.getMaterials().isEmpty()) {
                            if (workDto.getMaterials().size() > 0) {
                                for (Material material : workDto.getMaterials()) {
                                    Material materialIns = new Material();
                                    materialIns.setWork(workDb);
                                    materialIns.setValue(material.getValue());
                                    materialIns.setSubtot(material.getSubtot());
                                    materialIns.setDescription(material.getDescription());
                                    materialIns.setAmount(material.getAmount());
                                    iMaterialRepo.save(materialIns);
                                }
                            }
                        }


                    }
                }
            }
            if (!requesterDto.getListEquipment().isEmpty()) {
                if (requesterDto.getListEquipment().size() > 0) {
                    for (Equipment equipment : requesterDto.getListEquipment()) {
                        Equipment equipmentIns = new Equipment();
                        equipmentIns.setRequester(requesterDb);
                        equipmentIns.setEquipment(equipment.getEquipment());
                        equipmentIns.setNumberEquipments(equipment.getNumberEquipments());
                        equipmentIns.setDayValue(equipment.getDayValue());
                        equipmentIns.setNumberDays(equipment.getNumberDays());
                        equipmentIns.setSubtDayValue(equipment.getSubtDayValue());
                        equipmentIns.setHourValue(equipment.getHourValue());
                        equipmentIns.setNumberHours(equipment.getNumberHours());
                        equipmentIns.setSubtHourValue(equipment.getSubtHourValue());
                        equipmentIns.setTotalEquipment(equipment.getTotalEquipment());
                        iEquipmentRepo.save(equipmentIns);


                    }
                }
            }
            if (!requesterDto.getListTransport().isEmpty()) {
                if (requesterDto.getListTransport().size() > 0) {
                    for (Transport transport : requesterDto.getListTransport()) {
                        Transport transportIns = new Transport();
                        transportIns.setRequester(requesterDb);
                        transportIns.setDescription(transport.getDescription());
                        transportIns.setAmount(transport.getAmount());
                        transportIns.setValue(transport.getValue());
                        transportIns.setSubtot(transport.getSubtot());
                        iTransportRepo.save(transportIns);
                    }

                }
            }
            new PdfUtil().getPdfRequest(response, requesterDto);
            return true;
        } catch (Exception e) {

            logger.error("ERROR RequesterImplService.addRequester" + e.getMessage());
            return false;
        }

    }

    @Override
    public List<SeeRequester_dto> getRequests(int idStatus) {
        //List<SeeRequester_dto> inactivos = getRequestsByStatus(2);
        return getRequestsByStatus(idStatus);
       // List<SeeRequester_dto> resp = new ArrayList<>(inactivos);
        //resp.addAll(activos);


        //return resp;
    }


    //Metodo anterior
    public boolean deleteRequest_(int idRequest) {
        List<Work> works = iWorkRepo.findByRequester(new Requester(idRequest));
        if (works.size() > 0) {
            for (Work work : works) {
                iMaterialRepo.deleteByWork(work);
                iWorkRepo.delete(work);
            }
        }
        List<Transport> transports = iTransportRepo.findByRequester(new Requester(idRequest));
        if (transports.size() > 0) {
            for (Transport transport : transports) {
                iTransportRepo.delete(transport);
            }
        }

        List<Equipment> equipments = iEquipmentRepo.findByRequester(new Requester(idRequest));
        if (equipments.size() > 0) {
            for (Equipment equipment : equipments) {
                iEquipmentRepo.delete(equipment);
            }
        }

        iRequesterRepo.deleteById(idRequest);
        return true;
    }

    /** * Elimina una solicitud y todos los trabajos, materiales, transportes y equipos asociados.*/
    @Transactional
    @Override
    public boolean deleteRequest(int idRequest) {
        // Crear el objeto Requester solo una vez.
        Requester requester = new Requester(idRequest);

        // 1. Eliminar todos los trabajos y sus materiales asociados.
        List<Work> works = iWorkRepo.findByRequester(requester);
        if (!works.isEmpty()) {
            // Eliminar todos los materiales asociados a los trabajos.
            works.forEach(work -> iMaterialRepo.deleteByWork(work));

            // Eliminar todos los trabajos.
            iWorkRepo.deleteAll(works);
        }
        // 2. Eliminar todos los transportes asociados.

        List<Transport> transports = iTransportRepo.findByRequester(requester);
        if (!transports.isEmpty()) {
            // Eliminar todos los transportes.
            iTransportRepo.deleteAll(transports);
        }
        // 3. Eliminar todos los equipos asociados.

        List<Equipment> equipments = iEquipmentRepo.findByRequester(requester);
        if (!equipments.isEmpty()) {
            // Eliminar todos los equipos.
            iEquipmentRepo.deleteAll(equipments);
        }
        // 4. Finalmente, eliminar la solicitud.
        iRequesterRepo.deleteById(idRequest);

        return true;
    }


    @Override
    public Customer validNoDoc(String noDoc) {
        Optional<Customer> customer = iCustomerRepo.findByDocumentNumber(noDoc);
        if (customer.isPresent()) {
            return customer.get();
        }
        return null;
    }

    @Transactional
    @Override
    public int addUpdateEquipment(Equipment equipment, int idRequest) {
        Optional<Requester> requester = iRequesterRepo.findById(idRequest);
        if (requester.isPresent()) {
            requester.get().setTotalEquipment(requester.get().getTotalEquipment() + equipment.getTotalEquipment());
            requester.get().setTotalValueRequest(requester.get().getTotalValueRequest() + equipment.getTotalEquipment());
            iRequesterRepo.save(requester.get());
            equipment.setRequester(requester.get());
            return iEquipmentRepo.save(equipment).getId();
        }

        return 0;
    }

    @Override
    public boolean deleteUpdateEquipment(int idEquip) {
        Optional<Equipment> equipment = iEquipmentRepo.findById(idEquip);
        if (equipment.isPresent()) {
            Requester requester = equipment.get().getRequester();
            requester.setTotalEquipment(requester.getTotalEquipment() - equipment.get().getTotalEquipment());
            requester.setTotalValueRequest(requester.getTotalValueRequest() - equipment.get().getTotalEquipment());
            iRequesterRepo.save(requester);
            iEquipmentRepo.deleteById(equipment.get().getId());


            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateEquipment(Equipment equipment) {
        Optional<Equipment> equipmentUpd = iEquipmentRepo.findById(equipment.getId());
        if (equipmentUpd.isPresent()) {
            equipmentUpd.get().setEquipment(equipment.getEquipment());
            equipmentUpd.get().setNumberEquipments(equipment.getNumberEquipments());
            equipmentUpd.get().setDayValue(equipment.getDayValue());
            equipmentUpd.get().setNumberDays(equipment.getNumberDays());
            equipmentUpd.get().setSubtDayValue(equipment.getSubtDayValue());
            equipmentUpd.get().setHourValue(equipment.getHourValue());
            equipmentUpd.get().setNumberHours(equipment.getNumberHours());
            equipmentUpd.get().setSubtHourValue(equipment.getSubtHourValue());
            equipmentUpd.get().setTotalEquipment(equipment.getSubtDayValue()+equipment.getSubtHourValue());
            Requester requesterUpd = equipmentUpd.get().getRequester();
            Integer totalEquipment = iEquipmentRepo.getTotalEquipmentByRequester(requesterUpd.getId());
            requesterUpd.setTotalEquipment((totalEquipment != null) ? totalEquipment : 0);
            requesterUpd.setTotalValueRequest(requesterUpd.getTotalWork() + requesterUpd.getTotalMater() + requesterUpd.getTotalEquipment() + requesterUpd.getTotalTransport());
            iRequesterRepo.save(requesterUpd);
            iEquipmentRepo.save(equipmentUpd.get());

            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public int addUpdateTransport(Transport transport,int idRequest) {
        int idResp=0;
        Optional<Requester> requester=iRequesterRepo.findById(idRequest);
        if(requester.isPresent()){
            transport.setRequester(requester.get());
            idResp=iTransportRepo.save(transport).getId();
            iTransportRepo.flush();
            requester.get().setTotalTransport(0);
          List<Transport>transportsRequest=iTransportRepo.findByRequester(requester.get());
          if(transportsRequest.isEmpty()){
              requester.get().setTotalTransport(0);
          }else{
              requester.get().setTotalTransport(0);
              for (Transport transportbd:transportsRequest) {
                  requester.get().setTotalTransport(requester.get().getTotalTransport()+transportbd.getSubtot());
              }
          }
          requester.get().setTotalValueRequest(requester.get().getTotalWork()+requester.get().getTotalEquipment()+requester.get().getTotalTransport()+requester.get().getTotalMater());
          iRequesterRepo.save(requester.get());


        }
        return idResp;
    }

    @Transactional
    @Override
    public boolean deleteUpdateTransport(int idTransport) {
        Optional<Transport> transport=iTransportRepo.findById(idTransport);
        if(transport.isPresent()){
            Optional<Requester> requester=iRequesterRepo.findById(transport.get().getRequester().getId());
            if(requester.isPresent()){
                requester.get().setTotalTransport(0);
                iTransportRepo.delete(transport.get());
                iTransportRepo.flush();
                List<Transport>transports=iTransportRepo.findByRequester(requester.get());
                if(transports.size()>0){
                    for (Transport transp:transports) {
                        requester.get().setTotalTransport(requester.get().getTotalTransport()+transp.getSubtot());
                    }
                }
                requester.get().setTotalValueRequest(requester.get().getTotalWork()+requester.get().getTotalMater()+requester.get().getTotalTransport()+requester.get().getTotalEquipment());
                iRequesterRepo.save(requester.get());
                return true;
            }
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateTransport(Transport transport) {
        Optional<Transport> transportUpd=iTransportRepo.findById(transport.getId());
        if(transportUpd.isPresent()){
            transportUpd.get().setDescription(transport.getDescription());
            transportUpd.get().setAmount(transport.getAmount());
            transportUpd.get().setValue(transport.getValue());
            transportUpd.get().setSubtot(transport.getSubtot());
            Optional<Requester> requester=iRequesterRepo.findById(transportUpd.get().getRequester().getId());
            iTransportRepo.save(transportUpd.get());
            iTransportRepo.flush();
            if(requester.isPresent()){
                requester.get().setTotalTransport(0);
                List<Transport>transports=iTransportRepo.findByRequester(requester.get());
                if(transports.size()>0){
                    for (Transport transp:transports) {
                        requester.get().setTotalTransport(requester.get().getTotalTransport()+transp.getSubtot());
                    }
                }
                requester.get().setTotalValueRequest(requester.get().getTotalWork()+requester.get().getTotalMater()+requester.get().getTotalEquipment()+requester.get().getTotalTransport());
                iRequesterRepo.save(requester.get());
                return true;
            }


        }
        return false;
    }

    @Override
    public boolean startWorks(int idRequester) {
        Optional<Requester> requester=iRequesterRepo.findById(idRequester);
        if(requester.isPresent()){
            requester.get().setStatusRequ(new StatusRequ(1));
            requester.get().setDateStartRequest(new Date());
            iRequesterRepo.save(requester.get());
            return true;
        }
        return false;
    }

    @Override
    public void reprintRequester(HttpServletResponse reponse, int idRequ) {
        Optional<Requester> requester=iRequesterRepo.findById(idRequ);
        if(requester.isPresent()){
            Requester_Dto requesterDto=new Requester_Dto();
            requesterDto.setId(requester.get().getId());
            requesterDto.setNumberDoc(requester.get().getCustomer().getDocumentNumber());
            requesterDto.setNames(requester.get().getCustomer().getNames());
            requesterDto.setSurnames(requester.get().getCustomer().getSurnames());
            requesterDto.setPhoneNumber(requester.get().getCustomer().getPhoneNumber());
            requesterDto.setAddress(requester.get().getCustomer().getAddress());
            requesterDto.setDepartament(requester.get().getCustomer().getMunicipality().getDepartament().getId());
            requesterDto.setIdMunicipality(requester.get().getCustomer().getMunicipality().getId());
            requesterDto.setMunicipalityObj(requester.get().getCustomer().getMunicipality());
            requesterDto.setEmail(requester.get().getCustomer().getEmail());
            requesterDto.setDateLimit(requester.get().getDateLimit().toString());
            List<Work_Dto>listWorkDto=new ArrayList<>();
            List<Work>listWork=iWorkRepo.findByRequester(requester.get());
            if(!listWork.isEmpty()){
                for (Work work:listWork) {
                    Work_Dto workDto=new Work_Dto();
                    workDto.setId(work.getId());
                    workDto.setDescription(work.getDescription());
                    workDto.setCostManp(work.getCostManp());
                    workDto.setNumberManp(work.getNumberManp());
                    workDto.setTypeManp(work.getTypeManp());
                    workDto.setLaborCost(work.getLaborCost());
                    workDto.setMaterialCost(work.getMaterialCost());
                    workDto.setMaterials(iMaterialRepo.findByWork(work));
                    workDto.setAddress(work.getAddres());
                    workDto.setIdRequest(work.getRequester().getId());
                    listWorkDto.add(workDto);
                }
                requesterDto.setListWorksToBeDone(listWorkDto);
                requesterDto.setSubtWorks(requester.get().getTotalWork());
                requesterDto.setListEquipment(iEquipmentRepo.findByRequester(requester.get()));
                requesterDto.setListTransport(iTransportRepo.findByRequester(requester.get()));
                requesterDto.setTotalRequest(requester.get().getTotalValueRequest());
                new PdfUtil().getPdfRequest(reponse, requesterDto);
            }




        }

    }

    @Override
    public Requester_Dto getRequestById(int idRequest) {
        Optional<Requester> requester=iRequesterRepo.findById(idRequest);
        if(requester.isPresent()){
            Requester_Dto requesterDto=new Requester_Dto();
            requesterDto.setId(requester.get().getId());
            requesterDto.setNumberDoc(requester.get().getCustomer().getDocumentNumber());
            requesterDto.setNames(requester.get().getCustomer().getNames());
            requesterDto.setSurnames(requester.get().getCustomer().getSurnames());
            requesterDto.setPhoneNumber(requester.get().getCustomer().getPhoneNumber());
            requesterDto.setAddress(requester.get().getAddress());
            requesterDto.setDepartament(requester.get().getCustomer().getMunicipality().getDepartament().getId());
            requesterDto.setMunicipalityObj(requester.get().getCustomer().getMunicipality());
            requesterDto.setEmail(requester.get().getCustomer().getEmail());
            requesterDto.setDateLimit(requester.get().getDateLimit().toString());
            return requesterDto;
        }
        return null;
    }

    @Override
    public Requester_Dto updateRequest(Requester_Dto requesterDto) {
        Optional<Requester> requester=iRequesterRepo.findById(requesterDto.getId());
        if(requester.isPresent()){
            Optional<Customer> customer=iCustomerRepo.findById(requester.get().getCustomer().getId());
            if(customer.isPresent()){
                customer.get().setDocumentNumber(requesterDto.getNumberDoc());
                customer.get().setNames(requesterDto.getNames());
                customer.get().setSurnames(requesterDto.getSurnames());
                customer.get().setPhoneNumber(requesterDto.getPhoneNumber());
                customer.get().setEmail(requesterDto.getEmail());
                iCustomerRepo.save(customer.get());
            }

            requester.get().setAddress(requesterDto.getAddress());
            Optional<Municipality> municipality=iMunicipalityRepo.findById(requesterDto.getMunicipalityObj().getId());
            if(municipality.isPresent()){
                Optional<Customer> customer1=iCustomerRepo.findById(requester.get().getCustomer().getId());
                if(customer1.isPresent()){
                    customer1.get().setMunicipality(municipality.get());
                    iCustomerRepo.save(customer1.get());
                    requesterDto.setMunicipalityObj(customer1.get().getMunicipality());
                }


            }
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                requester.get().setDateLimit(formatter.parse(requesterDto.getDateLimit()));
            } catch (ParseException e) {
                logger.error("ERROR RequesterImplService.updateRequest "+e.getMessage());
            }
            iRequesterRepo.save(requester.get());
            return requesterDto;

        }

        return null;
    }

    public List<SeeRequester_dto> getRequestsByStatus(int idStatus) {
        SimpleDateFormat formatFecha = new SimpleDateFormat("yyyy/MM/dd");
        List<SeeRequester_dto> seeRequestsDtos = new ArrayList<>();
        List<Requester> requesters = iRequesterRepo.findByStatusRequ(new StatusRequ(idStatus)).orElse(null);
        if (!requesters.isEmpty()) {
            if (requesters.size() > 0) {
                for (Requester requester : requesters) {
                    SeeRequester_dto seeRequesterDto = new SeeRequester_dto(requester.getId(), formatFecha.format(requester.getDateInit()), requester.getCustomer().getNames(),requester.getCustomer().getSurnames(), requester.getCustomer().getDocumentNumber(), requester.getCustomer().getPhoneNumber(), requester.getCustomer().getMunicipality().getName(), requester.getCustomer().getAddress(), requester.getTotalWork(), requester.getTotalEquipment(), requester.getTotalMater(), requester.getTotalTransport(), requester.getTotalValueRequest());
                    seeRequesterDto.setDateLimit(requester.getDateLimit().toString());
                    if(requester.getDateStartRequest()!=null){
                        seeRequesterDto.setDateStartRequest(requester.getDateStartRequest().toString());
                    }

                    List<Work> works = iWorkRepo.findByRequester(requester);
                    List<Work_Dto> worksDto = new ArrayList<>();
                    if (works.size() > 0) {
                        for (Work work : works) {
                            Work_Dto workDto = new Work_Dto();
                            workDto.setId(work.getId());
                            workDto.setDescription(work.getDescription());
                            workDto.setCostManp(work.getCostManp());
                            workDto.setNumberManp(work.getNumberManp());
                            workDto.setTypeManp(work.getTypeManp());
                            workDto.setLaborCost(work.getLaborCost());
                            workDto.setMaterialCost(work.getMaterialCost());
                            // workDto.setMunicipalityObj(work.getMunicipality());
                            workDto.setAddress(work.getAddres());
                            workDto.setMaterials(iMaterialRepo.findByWork(work));
                            worksDto.add(workDto);
                        }
                    }
                    seeRequesterDto.setWorks(worksDto);
                    seeRequesterDto.setEquipments(iEquipmentRepo.findByRequester(requester));
                    seeRequesterDto.setTransports(iTransportRepo.findByRequester(requester));
                    seeRequestsDtos.add(seeRequesterDto);
                }
            }
        }
        return seeRequestsDtos;
    }

    public Municipality getMunicipalityObj(int idMunicipality) {
        return iMunicipalityRepo.findById(idMunicipality).get();
    }

    public Totals getTotalRequest(Requester_Dto requesterDto) {
        Totals totals = new Totals();
        int total = 0;
        if (!requesterDto.getListWorksToBeDone().isEmpty()) {
            if (requesterDto.getListWorksToBeDone().size() > 0) {
                for (Work_Dto work : requesterDto.getListWorksToBeDone()) {
                    totals.totalMater += work.getMaterialCost();
                    totals.totalWork += work.getLaborCost();
                }
            }
        }
        if (!requesterDto.getListTransport().isEmpty()) {
            if (requesterDto.getListTransport().size() > 0) {
                for (Transport transport : requesterDto.getListTransport()) {
                    totals.totalTransport += transport.getSubtot();
                }
            }
        }
        if (!requesterDto.getListEquipment().isEmpty()) {
            if (requesterDto.getListEquipment().size() > 0) {
                for (Equipment equipment : requesterDto.getListEquipment()) {
                    totals.totalEquipment += (equipment.getSubtHourValue() + equipment.getSubtDayValue());

                }
            }
        }

        totals.totalRequest = totals.totalMater + totals.totalWork + totals.totalEquipment + totals.totalTransport;
        return totals;
    }


    public static class Totals {

        private static int totalWork;
        private static int totalMater;
        private static int totalEquipment;
        private static int totalTransport;
        private static int totalRequest;

    }

}
