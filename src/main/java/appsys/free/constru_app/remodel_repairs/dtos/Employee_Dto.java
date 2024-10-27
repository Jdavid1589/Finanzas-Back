package appsys.free.constru_app.remodel_repairs.dtos;

import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.entities.ParametersPayroll;
import lombok.Data;

import java.util.List;

@Data
public class Employee_Dto {
 private String documentNumber;
 private String names;
 private String surnames;
 private String phoneNumber;
 private String address;
 private String email;
 private boolean enable;
 private int idMunicipality; // Para el ID de la municipalidad
 private int idParametersPayroll; // Para el ID de ParametersPayroll
}


