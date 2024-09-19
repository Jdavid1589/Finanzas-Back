package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.dtos.Material_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import org.springframework.data.jpa.repository.Query;

public interface IWorkService {

    boolean updateWork(Work_Dto work);

    Long add_updateWork(Work_Dto work);

    boolean deleteWorkUpdate(Work_Dto work);

    Long addMaterialWork(Material_dto material);

    boolean del_materialUpd(Material material);

    boolean updateMateriaL(Material_dto material);


}
