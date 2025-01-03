package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import appsys.free.constru_app.remodel_repairs.entities.Workday;

import java.util.List;

public interface IWorkdayService {



    Workday addWorkday(Workday workday);

    Workday getWorkdayById(int id);

    List<Workday> getWorkday();


}
