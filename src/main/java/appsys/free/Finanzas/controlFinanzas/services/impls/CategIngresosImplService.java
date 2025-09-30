package appsys.free.Finanzas.controlFinanzas.services.impls;

import appsys.free.Finanzas.controlFinanzas.entities.CategoriaIngresos;
import appsys.free.Finanzas.controlFinanzas.repositories.ICategIngresoRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.ICategIngresosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategIngresosImplService implements ICategIngresosService {
    private static final Logger logger = LoggerFactory.getLogger(CategIngresosImplService.class);
    @Autowired
    ICategIngresoRepo iCategoriaRepo;

      @Override
    public List<CategoriaIngresos> getAllCategorias() {
        try {
        return iCategoriaRepo.findAll();

        } catch (Exception e) {
            // Manejo de excepciones: Registrar o manejar la excepción según sea necesario
            System.err.println("Error retrieving Categorias: " + e.getMessage());
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }


    @Override
    public void save(CategoriaIngresos categoria) {
    iCategoriaRepo.save(categoria);

    }

    @Override
    public Optional<CategoriaIngresos> findById(Long id) {
        return iCategoriaRepo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        iCategoriaRepo.deleteById(id);
    }


}
