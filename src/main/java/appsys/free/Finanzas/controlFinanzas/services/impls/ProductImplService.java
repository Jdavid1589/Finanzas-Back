package appsys.free.Finanzas.controlFinanzas.services.impls;

import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;
import appsys.free.Finanzas.controlFinanzas.repositories.IProductoRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductImplService implements IProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductImplService.class);

    @Autowired
    IProductoRepo iProductoRepo;


    @Override
    public List<Producto> getAllProdut() {
        return iProductoRepo.findAll();
    }

    @Override
    public Producto save(Productos_Dto productos_dto) {
        Producto producto = Producto.builder()
                .id(productos_dto.getId())
                .nombreProducto(productos_dto.getNombreProducto())
                .precioEstimado(productos_dto.getPrecioEstimado())
                .categoria(productos_dto.getCategoria())
                .build();
        return iProductoRepo.save(producto);
    }



    @Override
    public Optional<Producto> getProductById(Long id) {
        return iProductoRepo.findById(id);
    }



    @Override
    public void deleteById(Long id) {
        iProductoRepo.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return iProductoRepo.existsById(id);
    }


}
