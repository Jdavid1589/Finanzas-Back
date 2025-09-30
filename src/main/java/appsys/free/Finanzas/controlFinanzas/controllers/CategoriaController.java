package appsys.free.Finanzas.controlFinanzas.controllers;


import appsys.free.Finanzas.controlFinanzas.dtos.Categoria_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.ICategoriaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/Categ")
@CrossOrigin(origins = "*")
public class CategoriaController {

    private static final Logger logger = LoggerFactory.getLogger(CategoriaController.class);
    @Autowired
    ICategoriaService iCategoriaService;

    /* - List for ID Product */
    @GetMapping("listCategId/{id}")
    public ResponseEntity<?> showById(@PathVariable Long id) {
        Optional<CategoriaGastos> categoriaOptional= iCategoriaService.findById(id);

        /* */
        if (categoriaOptional.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar, no existe!!")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        CategoriaGastos categoria = categoriaOptional.get();

        Categoria_Dto categoria_dto = Categoria_Dto.builder()
                .id(categoria.getId())
                .nombreCategoria(categoria.getNombreCategoria())
                .build();


        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(categoria_dto)
                        .build(),
                HttpStatus.OK);
    }


    @GetMapping("/listCateg")
    public ResponseEntity<?> showAll() {
        List<CategoriaGastos> getList = iCategoriaService.getAllCategorias();

        // Verifica si la lista obtenida es nula o vacía
        if (getList.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar, no existe!!")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }


        // Convierte la lista de productos a una lista de DTOs
        List<Categoria_Dto> CategList = iCategoriaService.getAllCategorias()
                .stream()
                .map(categoria -> Categoria_Dto.builder()
                        .id(categoria.getId())
                        .nombreCategoria(categoria.getNombreCategoria())
                       .build())
                .toList();

        // Devuelve una respuesta con la lista de productos
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("") // Establece el mensaje en una cadena vacía
                        .object(CategList) // Establece el objeto en la lista de productos
                        .build()
                , HttpStatus.OK); // Devuelve el estado HTTP 200 (OK)
    }

    /* Add */
    @PostMapping("/addGastos")
    public ResponseEntity<?> create(@RequestBody CategoriaGastos categoriaGastos) {
        try {
            // El service debe encargarse de convertir el DTO a entidad y guardarlo
            CategoriaGastos categoriaGastSave = iCategoriaService.save(categoriaGastos);

            // Convierte la entidad guardada de nuevo a DTO para la respuesta
            Categoria_Dto respuestaDto = Categoria_Dto.builder()
                    .id(categoriaGastSave.getId())
                    .nombreCategoria(categoriaGastSave.getNombreCategoria())
                    .build();

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Guardado correctamente")
                            .object(respuestaDto)
                            .build(),
                    HttpStatus.CREATED);

        } catch (DataAccessException exDt) {
            // Para errores de base de datos, usa INTERNAL_SERVER_ERROR
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error al guardar: " + exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }






}
