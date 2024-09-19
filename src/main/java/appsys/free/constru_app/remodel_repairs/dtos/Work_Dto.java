package appsys.free.constru_app.remodel_repairs.dtos;

import appsys.free.constru_app.remodel_repairs.entities.Material;
import appsys.free.constru_app.remodel_repairs.entities.Municipality;
import appsys.free.constru_app.remodel_repairs.entities.Requester;
import lombok.Data;

import java.util.List;

@Data
public class Work_Dto {
 private Long id;
    private String description;
    private int costManp;
    private int numberManp;
    private String typeManp;
    private int laborCost;
    private int materialCost;
    private List<Material> materials;

    private String address;
    private int idRequest;
}
