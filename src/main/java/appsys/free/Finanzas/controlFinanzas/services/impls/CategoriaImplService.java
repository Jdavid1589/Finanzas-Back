package appsys.free.Finanzas.controlFinanzas.services.impls;

import appsys.free.Finanzas.controlFinanzas.dtos.Categoria_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import appsys.free.Finanzas.controlFinanzas.repositories.ICategoriaRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.ICategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CategoriaImplService implements ICategoriaService {
    private static final Logger logger = LoggerFactory.getLogger(CategoriaImplService.class);
    @Autowired
    ICategoriaRepo iCategoriaRepo;

      @Override
    public List<CategoriaGastos> getAllCategorias() {
        try {
        return iCategoriaRepo.findAll();

        } catch (Exception e) {
            // Manejo de excepciones: Registrar o manejar la excepción según sea necesario
            System.err.println("Error retrieving Categorias: " + e.getMessage());
            return Collections.emptyList(); // Devolver una lista vacía en caso de error
        }
    }

    @Override
    public CategoriaGastos save(CategoriaGastos categoria) {
        CategoriaGastos categoriaGastos= CategoriaGastos.builder()
                .id(categoria.getId())
                .nombreCategoria(categoria.getNombreCategoria())
                .build();
        return iCategoriaRepo.save(categoriaGastos);
    }


    @Override
    public Optional<CategoriaGastos> findById(Long id) {
        return iCategoriaRepo.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        iCategoriaRepo.deleteById(id);
    }


}
