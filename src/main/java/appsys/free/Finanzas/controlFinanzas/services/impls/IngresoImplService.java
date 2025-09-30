package appsys.free.Finanzas.controlFinanzas.services.impls;

import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.CategoriaIngresos;
import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;
import appsys.free.Finanzas.controlFinanzas.repositories.IIngresoRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IProductoRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IIngresosService;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class IngresoImplService implements IIngresosService {

    private static final Logger logger = LoggerFactory.getLogger(IngresoImplService.class);

    @Autowired
    IIngresoRepo iIngresoRepo;

    @Override
    public List<Ingresos> getAllIngresos() {
        return iIngresoRepo.findAll();
    }

    @Override
    public Ingresos save(Ingreso_Dto ingreso_dto) {
        Ingresos ingresos;
        if (ingreso_dto.getId() != null && iIngresoRepo.existsById(ingreso_dto.getId())) {
            // Es update: recupera la entidad y actualiza campos
            ingresos = iIngresoRepo.findById(ingreso_dto.getId()).get();
            ingresos.setFechaIngreso(ingreso_dto.getFechaIngreso());
            ingresos.setDescripcion(ingreso_dto.getDescripcion());
            ingresos.setCantidad(ingreso_dto.getCantidad());
            ingresos.setCategIngresos(ingreso_dto.getCategoriaIngresos());
        } else {
            // Es create: crea nueva entidad
            ingresos = Ingresos.builder()
                    .fechaIngreso(ingreso_dto.getFechaIngreso())
                    .descripcion(ingreso_dto.getDescripcion())
                    .cantidad(ingreso_dto.getCantidad())
                    .categIngresos(ingreso_dto.getCategoriaIngresos())
                    .build();
        }
        return iIngresoRepo.save(ingresos);
    }

    @Override
    public Optional<Ingresos> getIngresoById(Long id) {
        return iIngresoRepo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        iIngresoRepo.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return iIngresoRepo.existsById(id);
    }


}
