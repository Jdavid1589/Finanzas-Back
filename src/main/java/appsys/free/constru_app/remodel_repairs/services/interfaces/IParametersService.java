package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.dtos.Material_dto;
import appsys.free.constru_app.remodel_repairs.dtos.Work_Dto;
import appsys.free.constru_app.remodel_repairs.entities.Employee;
import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;

import java.util.List;

public interface IParametersService {

   boolean updateParameters(ParametersPayroll parametersPayroll);

    ParametersPayroll addParameter(ParametersPayroll parametersPayroll);

    ParametersPayroll getParametersById(int id);

    List<ParametersPayroll> getParameters();

}
