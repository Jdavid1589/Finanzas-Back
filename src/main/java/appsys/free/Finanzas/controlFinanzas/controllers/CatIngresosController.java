package appsys.free.Finanzas.controlFinanzas.controllers;


import appsys.free.Finanzas.controlFinanzas.dtos.CategoriaIngresos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.CategoriaIngresos;
import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.ICategIngresosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/CategIngresos")
@CrossOrigin(origins = "*")
public class CatIngresosController {

    private static final Logger logger = LoggerFactory.getLogger(CatIngresosController.class);
    @Autowired
    ICategIngresosService iCategoriaService;

    /* - List for ID  */
    @GetMapping("listCatIngresosId/{id}")
    public ResponseEntity<?> showById(@PathVariable Long id) {
        Optional<CategoriaIngresos> categoriaOptional= iCategoriaService.findById(id);

        /* */
        if (categoriaOptional.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar, no existe!!")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        CategoriaIngresos categoria = categoriaOptional.get();

        CategoriaIngresos_Dto categoria_dto = CategoriaIngresos_Dto.builder()
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
        List<CategoriaIngresos> getList = iCategoriaService.getAllCategorias();

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
        List<CategoriaIngresos_Dto> CategList = iCategoriaService.getAllCategorias()
                .stream()
                .map(categoria -> CategoriaIngresos_Dto.builder()
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








}
