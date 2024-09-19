package appsys.free.constru_app.remodel_repairs.services.interfaces;

import appsys.free.constru_app.remodel_repairs.entities.Municipality;

import java.util.List;

public interface IMunicipalityService {

    List<Municipality> getMunicipalities(int idDeparatment);
}
