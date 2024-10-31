package appsys.free.constru_app.remodel_repairs.dtos;



import lombok.Data;


@Data
public class Employee_Dto {
 private String documentNumber;
 private String names;
 private String surnames;
 private String phoneNumber;
 private String address;
 private String email;
 private boolean enable;
 private int idMunicipality;
 private int idParametersPayroll;
}


