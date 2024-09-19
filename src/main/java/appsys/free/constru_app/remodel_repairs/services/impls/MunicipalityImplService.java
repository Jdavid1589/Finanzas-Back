package appsys.free.constru_app.remodel_repairs.services.impls;

import appsys.free.constru_app.remodel_repairs.entities.Departament;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.repositories.IMunicipalityRepo;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IMunicipalityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MunicipalityImplService implements IMunicipalityService {

    @Autowired
    IMunicipalityRepo iMunicipalityRepo;
    @Override
    public List<Municipality> getMunicipalities(int idDepartamet) {

        return iMunicipalityRepo.findByDepartament(new Departament(idDepartamet));
    }
}
