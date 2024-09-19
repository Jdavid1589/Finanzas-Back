package appsys.free.constru_app.remodel_repairs.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Material_dto {
    private Long id;
    private String description;
    private int amount;
    private int value;
    private int subtot;
    private Long idWork;
}
