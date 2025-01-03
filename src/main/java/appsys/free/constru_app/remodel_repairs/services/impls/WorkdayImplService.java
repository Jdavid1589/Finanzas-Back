package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import appsys.free.constru_app.remodel_repairs.entities.Workday;
import appsys.free.constru_app.remodel_repairs.repositories.IParametersRepo;
import appsys.free.constru_app.remodel_repairs.repositories.IWorkdayRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IParametersService;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IWorkdayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class WorkdayImplService implements IWorkdayService {

    private static final Logger logger = LoggerFactory.getLogger(WorkdayImplService.class);
    @Autowired
    IWorkdayRepo iWorkdayRepo;

    @Override
    public Workday addWorkday(Workday workday) {
        return iWorkdayRepo.save(workday);

    }

    @Override
    public List<Workday> getWorkday() {
        return iWorkdayRepo.findAll();
    }


    @Override
    public Workday getWorkdayById(int id) {
        // Buscar el Workday en la base de datos por id
        Optional<Workday> workdayOptional = iWorkdayRepo.findById(id);
        // Si no se encuentra, devolver
        if (workdayOptional.isEmpty()) {
            return null;
        }
        Workday parameter = workdayOptional.get();
        parameter.setId(parameter.getId());
        parameter.setWeeklyHours(parameter.getWeeklyHours());

        return parameter;
    }







}


