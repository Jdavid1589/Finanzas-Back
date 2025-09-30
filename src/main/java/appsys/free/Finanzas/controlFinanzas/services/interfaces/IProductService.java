package appsys.free.Finanzas.controlFinanzas.services.interfaces;

import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Producto> getAllProdut();

   // void save (Producto producto);
    Producto save (Productos_Dto producto);
    Optional<Producto> getProductById(Long id);

    boolean existsById(Long id);

    void deleteById(Long id);





}
