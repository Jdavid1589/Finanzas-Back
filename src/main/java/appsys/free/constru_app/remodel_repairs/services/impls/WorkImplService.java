package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.dtos.Material_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.*;
import appsys.free.constru_app.remodel_repairs.repositories.*;
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

    @Autowired
    IEquipmentRepo iEquipmentRepo;

    @Autowired
    ITransportRepo iTransportRepo;

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
            // Eliminar el material
            iMaterialRepo.delete(resp.get());
            iMaterialRepo.flush(); // Asegura que la eliminación se ejecute inmediatamente

            // Actualizar los totales basados en el id del requester relational con el work
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

    @Override
    public Work_Dto getWorkById(Long id) {
        // Buscar el trabajo en la base de datos por id
        Optional<Work> workOptional = iWorkRepo.findById(id);

        // Si no se encuentra, devolver null
        if (!workOptional.isPresent()) {
            return null;
        }

        // Convertir la entidad Work a DTO
        Work work = workOptional.get();
        Work_Dto workDto = new Work_Dto();

        // Asignar los valores de la entidad al DTO
        workDto.setId(work.getId());
        workDto.setDescription(work.getDescription());
        workDto.setCostManp(work.getCostManp());
        workDto.setNumberManp(work.getNumberManp());
        workDto.setTypeManp(work.getTypeManp());
        workDto.setLaborCost(work.getLaborCost());
        workDto.setMaterialCost(work.getMaterialCost());
        workDto.setAddress(work.getAddres());

        return workDto;
    }


    //Metodo Ok Modificado
    public void updateTotals(int idRequest) {
        Optional<Requester> requesterOpt = iRequesterRepo.findById(idRequest);
        if (requesterOpt.isPresent()) {
            List<Work> works = iWorkRepo.findByRequester(requesterOpt.get());
            List<Equipment>equipmentList=iEquipmentRepo.findByRequester(requesterOpt.get());
            List<Transport>transportList=iTransportRepo.findByRequester(requesterOpt.get());
            int totalWork=0;
            int totalMater=0;
            if(works.size()>0){
                for (Work work:works) {
                    List<Material>materials=iMaterialRepo.findByWork(work);
                    if(materials.size()>0){
                        int valMaterial=0;
                        for (Material mater:materials) {
                            valMaterial+=mater.getSubtot();
                        }
                        work.setMaterialCost(valMaterial);
                    }else{
                        work.setMaterialCost(0);
                    }
                    iWorkRepo.save(work);
                    totalWork+=work.getLaborCost();
                    totalMater+=work.getMaterialCost();

                }
                requesterOpt.get().setTotalWork(totalWork);
                requesterOpt.get().setTotalMater(totalMater);

            }else{
                requesterOpt.get().setTotalWork(0);
                requesterOpt.get().setTotalMater(0);

            }
            if(equipmentList.size()>0){
                int totalEquipment=0;
                for (Equipment equipment:equipmentList) {
                    totalEquipment+=equipment.getTotalEquipment();
                }
                requesterOpt.get().setTotalEquipment(totalEquipment);
            }else{
                requesterOpt.get().setTotalEquipment(0);
            }
            if(transportList.size()>0){
                int valTransp=0;
                for (Transport transport:transportList) {
                    valTransp+=transport.getSubtot();

                }
                requesterOpt.get().setTotalTransport(valTransp);

            }else{
                requesterOpt.get().setTotalTransport(0);
            }
            requesterOpt.get().setTotalValueRequest(requesterOpt.get().getTotalWork()+requesterOpt.get().getTotalMater()+requesterOpt.get().getTotalEquipment()+requesterOpt.get().getTotalTransport());
            iRequesterRepo.save(requesterOpt.get());



        }


    }

    //Metodo OK, Refactorizado
    public void updateTotals_ok(int idRequest) {
        // Buscar el Requester (solicitante) en la base de datos mediante su ID.
        Optional<Requester> requesterOpt = iRequesterRepo.findById(idRequest);
        if (!requesterOpt.isPresent()) {
            return; // Salimos si no existe el requester
        }
        // Obtenemos el Requester (solicitante) de manera segura.
        Requester requester = requesterOpt.get();

        // 1. Calcular el costo total de trabajos y materiales.

        // Obtener todos los trabajos asociados al solicitante.
        List<Work> works = iWorkRepo.findByRequester(requester);
        int totalWork = 0;
        int totalMater = 0;

        // Si existen trabajos, recorremos la lista.
        if (!works.isEmpty()) {
            // Obtener todos los materiales asociados al trabajo actual.
            for (Work work : works) {
                int materialCost = iMaterialRepo.findByWork(work)
                        .stream()
                        .mapToInt(Material::getSubtot) // Sumar el subtotal de cada material.
                        .sum();

                // Asignar el costo total de materiales al trabajo actual.
                work.setMaterialCost(materialCost);

                // Guardar el trabajo actualizado en el repositorio.
                iWorkRepo.save(work);

                // Acumular los costos de la mano de obra y los materiales.
                totalWork += work.getLaborCost();
                totalMater += work.getMaterialCost();
            }
        }
        // Asignar los costos totales al solicitante.
        requester.setTotalWork(totalWork);
        requester.setTotalMater(totalMater);


        // 2. Calcular el costo total de equipos.
        // ---------------------------------------------------

        // Obtener todos los equipos asociados al solicitante.
        List<Equipment> equipmentList = iEquipmentRepo.findByRequester(requester);

        // Calcular el costo total de los equipos sumando el valor de cada equipo.
        int totalEquipment = equipmentList.stream()
                .mapToInt(Equipment::getTotalEquipment)
                .sum();

        // Asignar el costo total de equipos al solicitante.
        requester.setTotalEquipment(totalEquipment);

        // 3. Calcular el costo total de transporte.
        // ---------------------------------------------------

        // Obtener todas las entradas de transporte asociadas al solicitante.
        List<Transport> transportList = iTransportRepo.findByRequester(requester);

        // Calcular el costo total de transporte sumando el subtotal de cada transporte.
        int totalTransport = transportList.stream()
                .mapToInt(Transport::getSubtot)
                .sum();

        // Asignar el costo total de transporte al solicitante.
        requester.setTotalTransport(totalTransport);

        // 4. Calcular el valor total de la solicitud.
        // ---------------------------------------------------

        // El valor total de la solicitud es la suma de trabajos, materiales, equipos y transporte.
        int totalValueRequest = totalWork + totalMater + totalEquipment + totalTransport;

        // Asignar el valor total de la solicitud al solicitante.
        requester.setTotalValueRequest(totalValueRequest);

        //  5. Guardar el solicitante actualizado en el Requester.
        iRequesterRepo.save(requester);
    }

    /** * Actualiza los totales de trabajo, materiales, equipo y transporte de una solicitud. */
    public void updateTotalsx(int idRequest) {
        // Buscar el Requester (solicitante) en la base de datos mediante su ID.
        Optional<Requester> requesterOpt = iRequesterRepo.findById(idRequest);

        // Si el solicitante no existe, salimos del método.
        if (!requesterOpt.isPresent()) {
            return;
        }

        // Obtenemos el Requester (solicitante) de manera segura.
        Requester requester = requesterOpt.get();

        // ----------------------------------------------------
        // 1. Calcular el costo total de trabajos y materiales.
        // ----------------------------------------------------

        // Obtener todos los trabajos asociados al solicitante.
        List<Work> works = iWorkRepo.findByRequester(requester);
        int totalWork = 0;  // Acumulará el costo total de la mano de obra.
        int totalMater = 0; // Acumulará el costo total de los materiales.

        // Si existen trabajos, recorremos la lista.
        if (!works.isEmpty()) {
            for (Work work : works) {
                // Obtener todos los materiales asociados al trabajo actual.
                int materialCost = iMaterialRepo.findByWork(work)
                        .stream()
                        .mapToInt(Material::getSubtot) // Sumar el subtotal de cada material.
                        .sum();

                // Asignar el costo total de materiales al trabajo actual.
                work.setMaterialCost(materialCost);

                // Guardar el trabajo actualizado en el repositorio.
                iWorkRepo.save(work);

                // Acumular los costos de la mano de obra y los materiales.
                totalWork += work.getLaborCost();
                totalMater += work.getMaterialCost();
            }
        }

        // Asignar los costos totales al solicitante.
        requester.setTotalWork(totalWork);
        requester.setTotalMater(totalMater);

        // ---------------------------------------------------
        // 2. Calcular el costo total de equipos.
        // ---------------------------------------------------

        // Obtener todos los equipos asociados al solicitante.
        List<Equipment> equipmentList = iEquipmentRepo.findByRequester(requester);

        // Calcular el costo total de los equipos sumando el valor de cada equipo.
        int totalEquipment = equipmentList.stream()
                .mapToInt(Equipment::getTotalEquipment)
                .sum();

        // Asignar el costo total de equipos al solicitante.
        requester.setTotalEquipment(totalEquipment);

        // ---------------------------------------------------
        // 3. Calcular el costo total de transporte.
        // ---------------------------------------------------

        // Obtener todas las entradas de transporte asociadas al solicitante.
        List<Transport> transportList = iTransportRepo.findByRequester(requester);

        // Calcular el costo total de transporte sumando el subtotal de cada transporte.
        int totalTransport = transportList.stream()
                .mapToInt(Transport::getSubtot)
                .sum();

        // Asignar el costo total de transporte al solicitante.
        requester.setTotalTransport(totalTransport);

        // ---------------------------------------------------
        // 4. Calcular el valor total de la solicitud.
        // ---------------------------------------------------

        // El valor total de la solicitud es la suma de trabajos, materiales, equipos y transporte.
        int totalValueRequest = totalWork + totalMater + totalEquipment + totalTransport;

        // Asignar el valor total de la solicitud al solicitante.
        requester.setTotalValueRequest(totalValueRequest);

        // ---------------------------------------------------
        // 5. Guardar el solicitante con los nuevos valores.
        // ---------------------------------------------------

        // Guardar el solicitante actualizado en el repositorio.
        iRequesterRepo.save(requester);
    }


    //Anterior
    public void updateTotals_error(int idRequest) {
        Optional<Requester> requesterOpt = iRequesterRepo.findById(idRequest);
        if (requesterOpt.isPresent()) {
            List<Work> works = iWorkRepo.findByRequester(requesterOpt.get());
            if (!works.isEmpty()) {
                int costWorks = 0;
                int costTotalMaterials = 0;

                // Recalcular costos de trabajos y materiales
                for (Work work : works) {
                    List<Material> materials = iMaterialRepo.findByWork(work);
                    int materialCost = materials.stream().mapToInt(Material::getSubtot).sum();

                    // Actualizar el costo de materiales en el Work
                    work.setMaterialCost(materialCost);
                    iWorkRepo.save(work);  // Guardar el work actualizado
                    iWorkRepo.flush();     // Asegurar que se escriban los cambios

                    // Sumar el costo de los trabajos y materiales
                    costWorks += work.getLaborCost();
                    costTotalMaterials += work.getMaterialCost();
                }

                // Actualizar los totales en el Requester
                Requester requester = requesterOpt.get();
                requester.setTotalWork(costWorks);
                requester.setTotalMater(costTotalMaterials);
                requester.setTotalValueRequest(costWorks + costTotalMaterials +
                requester.getTotalTransport() + requester.getTotalEquipment());

                // Guardar el requester actualizado
                iRequesterRepo.save(requester);
                iRequesterRepo.flush(); // Asegurar que los cambios se guarden
            }
        }
    }





}


