package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.dtos.Material_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.Requester;
import appsys.free.constru_app.remodel_repairs.entities.Work;
import appsys.free.constru_app.remodel_repairs.repositories.IMaterialRepo;
import appsys.free.constru_app.remodel_repairs.repositories.IRequesterRepo;
import appsys.free.constru_app.remodel_repairs.repositories.IWorkRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IWorkService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WorkImplService implements IWorkService {

    private static final Logger logger = LoggerFactory.getLogger(WorkImplService.class);
    @Autowired
    IWorkRepo iWorkRepo;

    @Autowired
    IMaterialRepo iMaterialRepo;

    @Autowired
    IRequesterRepo iRequesterRepo;

    @Transactional
    @Override
    public boolean updateWork(Work_Dto work) {
        Optional<Work> workBd = iWorkRepo.findById(work.getId());
        if (workBd.isPresent()) {
            workBd.get().setDescription(work.getDescription());
            workBd.get().setCostManp(work.getCostManp());
            workBd.get().setNumberManp(work.getNumberManp());
            workBd.get().setTypeManp(work.getTypeManp());
            workBd.get().setLaborCost(work.getLaborCost());
            workBd.get().setAddres(work.getAddress());
            iWorkRepo.save(workBd.get());
            updateTotals(workBd.get().getRequester().getId());
            return true;

        }
        return false;
    }

    @Override
    public Long add_updateWork(Work_Dto work) {
        Work workBd = new Work();
        workBd.setDescription(work.getDescription());
        workBd.setCostManp(work.getCostManp());
        workBd.setNumberManp(work.getNumberManp());
        workBd.setTypeManp(work.getTypeManp());
        workBd.setLaborCost(work.getLaborCost());
        workBd.setAddres(work.getAddress());
        workBd.setRequester(new Requester(work.getIdRequest()));
        Long idWork=iWorkRepo.save(workBd).getId();
        updateTotals(work.getIdRequest());

        return idWork;
    }

    @Transactional
    @Override
    public boolean deleteWorkUpdate(Work_Dto work) {
        Optional<Work> workDelete = iWorkRepo.findById(work.getId());
        if (workDelete.isPresent()) {
            iMaterialRepo.deleteByWork(workDelete.get());
            iWorkRepo.deleteById(work.getId());
            iMaterialRepo.flush();
            iWorkRepo.flush();
            updateTotals(workDelete.get().getRequester().getId());
        }

        return true;
    }

    @Transactional
    @Override
    public Long addMaterialWork(Material_dto material_dto) {
        Long idMaterial;
        Material material = new Material();
        material.setDescription(material_dto.getDescription());
        material.setAmount(material_dto.getAmount());
        material.setValue(material_dto.getValue());
        material.setSubtot(material_dto.getSubtot());
        Optional<Work> workOpt = iWorkRepo.findById(material_dto.getIdWork());
        if (workOpt.isPresent()) {
            material.setWork(workOpt.get());
            Long idMaterialInsert = iMaterialRepo.save(material).getId();
            iMaterialRepo.flush();
            updateTotals(workOpt.get().getRequester().getId());
            return idMaterialInsert;
        } else {
            logger.error("ERROR No se pudo insertar Material  WorkImplService.addMaterialWork");
            return null;
        }


    }

    @Transactional
    @Override
    public boolean del_materialUpd(Material material) {
        Optional<Material> resp = iMaterialRepo.findById(material.getId());
        if (resp.isPresent()) {
            iMaterialRepo.delete(resp.get());
            iMaterialRepo.flush();
            updateTotals(resp.get().getWork().getRequester().getId());
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public boolean updateMateriaL(Material_dto material) {
        Optional<Material> resp=iMaterialRepo.findById(material.getId());
        if(resp.isPresent()){
            resp.get().setDescription(material.getDescription());
            resp.get().setValue(material.getValue());
            resp.get().setAmount(material.getAmount());
            resp.get().setSubtot(material.getSubtot());
            iMaterialRepo.save(resp.get());
            iMaterialRepo.flush();
            Optional<Work> workResp=iWorkRepo.findById(material.getIdWork());
            if(workResp.isPresent()){
                updateTotals(workResp.get().getRequester().getId());
            }
        }

        return true;
    }


    public void updateTotals(int idRequest) {
        Optional<Requester> requesterOpt = iRequesterRepo.findById(idRequest);
        if (requesterOpt.isPresent()) {
            List<Work> works = iWorkRepo.findByRequester(requesterOpt.get());
            if (!works.isEmpty()) {
                if (works.size() > 0) {
                    int costWorks = 0;
                    int costTotalMaterilals = 0;
                    for (Work work : works) {

                        List<Material> materials = iMaterialRepo.findByWork(work);
                        if (!materials.isEmpty()) {
                            if (materials.size() > 0) {
                                int materialCost = 0;
                                for (Material material : materials) {
                                    materialCost += material.getSubtot();

                                }
                                work.setMaterialCost(materialCost);
                                iWorkRepo.save(work);
                                iWorkRepo.flush();

                            } else {
                                work.setMaterialCost(0);
                                iWorkRepo.save(work);
                            }
                        } else {
                            work.setMaterialCost(0);
                            iWorkRepo.save(work);
                        }
                        costWorks += work.getLaborCost();
                        costTotalMaterilals += work.getMaterialCost();


                    }
                    requesterOpt.get().setTotalWork(costWorks);
                    requesterOpt.get().setTotalMater(costTotalMaterilals);
                    requesterOpt.get().setTotalValueRequest(requesterOpt.get().getTotalWork()+requesterOpt.get().getTotalMater()+requesterOpt.get().getTotalTransport()+requesterOpt.get().getTotalEquipment());
                    iRequesterRepo.save(requesterOpt.get());

                }else{

                    requesterOpt.get().setTotalWork(0);
                    requesterOpt.get().setTotalMater(0);
                    requesterOpt.get().setTotalValueRequest(requesterOpt.get().getTotalWork()+requesterOpt.get().getTotalMater()+requesterOpt.get().getTotalTransport()+requesterOpt.get().getTotalEquipment());
                    iRequesterRepo.save(requesterOpt.get());
                }


            }else{
                requesterOpt.get().setTotalWork(0);
                requesterOpt.get().setTotalMater(0);
                requesterOpt.get().setTotalValueRequest(requesterOpt.get().getTotalWork()+requesterOpt.get().getTotalMater()+requesterOpt.get().getTotalTransport()+requesterOpt.get().getTotalEquipment());
                iRequesterRepo.save(requesterOpt.get());
            }

        }


    }




}


