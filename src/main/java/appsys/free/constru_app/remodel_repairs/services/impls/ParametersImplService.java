package appsys.free.constru_app.remodel_repairs.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import appsys.free.constru_app.remodel_repairs.entities.*;
import appsys.free.constru_app.remodel_repairs.repositories.*;
import appsys.free.constru_app.remodel_repairs.services.interfaces.IParametersService;

import java.util.List;
import java.util.Optional;

@Service
public class ParametersImplService implements IParametersService {

    private static final Logger logger = LoggerFactory.getLogger(ParametersImplService.class);
    @Autowired
    IParametersRepo iParametersRepo;

    @Override
    public ParametersPayroll addParameter(ParametersPayroll parameter) {
        return iParametersRepo.save(parameter);

    }


    @Transactional
    @Override
    public boolean updateParameters(ParametersPayroll parameters) {
        Optional<ParametersPayroll> parametersBd = iParametersRepo.findById(parameters.getId());

        if (parametersBd.isPresent()) {
            ParametersPayroll existingParameters = parametersBd.get();
            existingParameters.setProfile(parameters.getProfile());
            existingParameters.setSuggestedPayroll(parameters.getSuggestedPayroll());
            existingParameters.setSocialSecurity(parameters.getSocialSecurity());

            // Guardar la entidad actualizada
            iParametersRepo.save(existingParameters);
            return true;
        }

        return false;
    }

    @Transactional
    @Override
    public boolean updateSocialSecurityForAll(Integer socialSecurityValue) {
        List<ParametersPayroll> allParameters = iParametersRepo.findAll();
        if (!allParameters.isEmpty()) {
            allParameters.forEach(param -> param.setSocialSecurity(socialSecurityValue));
            iParametersRepo.saveAll(allParameters);
            return true;
        }
        return false;
    }



    @Override
    public List<ParametersPayroll> getParameters() {
        return iParametersRepo.findAll();
    }


    @Override
    public ParametersPayroll getParametersById(int id) {
        // Buscar el Parameters en la base de datos por id
        Optional<ParametersPayroll> parametersOptional = iParametersRepo.findById(id);
        // Si no se encuentra, devolver null
        //if (!parametersOptional.isPresent()) asi testdata
        if (parametersOptional.isEmpty()) {
            return null;
        }
        ParametersPayroll parameter = parametersOptional.get();
        parameter.setId(parameter.getId());
        parameter.setProfile(parameter.getProfile());
        parameter.setSuggestedPayroll(parameter.getSuggestedPayroll());
        parameter.setSocialSecurity(parameter.getSocialSecurity());

        return parameter;
    }







}


