package appsys.free.Finanzas.controlFinanzas.controllers;


import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;


import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
/*import org.springframework.security.access.annotation.Secured;*/
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product")
@CrossOrigin(origins = "*")
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);
    @Autowired
    IProductService iProductService;

    /* - list Id Product */
    @GetMapping("/listProdId/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Producto> productOpt = iProductService.getProductById(id);

        if (!productOpt.isPresent()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar, no existe!!")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        Producto producto = productOpt.get();

        Productos_Dto productoDto = Productos_Dto.builder()
                .id(producto.getId())
                .nombreProducto(producto.getNombreProducto())
                .precioEstimado(producto.getPrecioEstimado())
                .categoria(producto.getCategoria())
                .build();

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("")
                        .object(productoDto)
                        .build(),
                HttpStatus.OK);
    }

    /* - list All Product */
    @GetMapping("/listProduct")
    public ResponseEntity<?> showAll() {
        List<Producto> getList = iProductService.getAllProdut();

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
        List<Productos_Dto> productList = iProductService.getAllProdut()
                .stream()
                .map(producto -> Productos_Dto.builder()
                        .id(producto.getId())
                        .nombreProducto(producto.getNombreProducto())
                        .precioEstimado(producto.getPrecioEstimado())
                        .categoria(producto.getCategoria())
                        .build())
                .toList();

        // Devuelve una respuesta con la lista de productos
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("") // Establece el mensaje en una cadena vacía
                        .object(productList) // Establece el objeto en la lista de productos
                        .build()
                , HttpStatus.OK); // Devuelve el estado HTTP 200 (OK)
    }

    /* - Add Product 1 */
     /*  @PostMapping("/addProd")
        public ResponseEntity<?> addProduct(@RequestBody Productos_Dto prod_dto) {

           if(prod_dto.getNombreProducto().isBlank()){
               return  ResponseEntity.badRequest().build();
            }
            try {
                 iProductService.save(Productos_Dto.builder()
                         .nombreProducto(prod_dto.getNombreProducto())
                         .precioEstimado(prod_dto.getPrecioEstimado())
                         .categoria(prod_dto.getCategoria())
                         .build());

                 return ResponseEntity.created(new URI("/api/product/addProd"))
                         .build();

                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Guardado correctamente")
                        .object(Productos_Dto.builder()
                                .id(productosSave.getId())
                                .nombreProducto(productosSave.getNombreProducto())
                                .precioEstimado(productosSave.getPrecioEstimado())
                                .categoriaIds(productosSave.getCategoria().getId())
                                .build())
                        .build(), HttpStatus.CREATED);

            } catch (DataAccessException exDt) {
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje(exDt.getMessage())
                        .object(null)
                        .build(), HttpStatus.METHOD_NOT_ALLOWED);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
        }*/

    /*Add Product*/
    @PostMapping("/addProd")
    public ResponseEntity<?> create(@RequestBody Productos_Dto productos_dto) {
        Producto productSave = null;
        try {
            productSave = iProductService.save(productos_dto);
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje("Guardado correctamente")
                    .object(Productos_Dto.builder()
                            .id(productSave.getId())
                            .nombreProducto(productSave.getNombreProducto())
                            .precioEstimado(productSave.getPrecioEstimado())
                            .categoria(productSave.getCategoria())
                            .build())
                    .build()
                    , HttpStatus.CREATED);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

    /*Update Product*/
   /* @PutMapping("/updproduct/{id}")
    public ResponseEntity<?> update_(@RequestBody Productos_Dto productos_dto, @PathVariable Long id) {
        Optional<Producto> getProductById = iProductService.getProductById(id);

        try {
            if (getProductById.isPresent()) {
                // Establecer el ID en el DTO para asegurar que se actualice el registro correcto
                productos_dto.setId(id);

                // Guardar la entidad actualizada usando el método save que mapea el DTO
                Producto productoActualizado = iProductService.save(productos_dto);

                return ResponseEntity.ok(MensajeResponse.builder()
                        .mensaje("Registro Actualizado con Exito")
                        .object(productoActualizado)
                        .build());

            } else {
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("El registro que intenta actualizar no se encuentra en la base de datos.")
                        .object(null)
                        .build(), HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(MensajeResponse.builder()
                    .mensaje(exDt.getMessage())
                    .object(null)
                    .build(), HttpStatus.METHOD_NOT_ALLOWED);
        }
    }*/


    /* - Update Product */
    @PutMapping("/updproduct/{id}")
    public ResponseEntity<?> update(@RequestBody Productos_Dto prod_dto, @PathVariable Long id) {
        Producto prodUpd = null;
        try {
            if (iProductService.existsById(id)) {
                prod_dto.setId(id);
                prodUpd = iProductService.save(prod_dto);
                return new ResponseEntity<>(MensajeResponse.builder()
                        .mensaje("Guardado correctamente")
                        .object(Productos_Dto.builder()
                                .id(prodUpd.getId())
                                .nombreProducto(prodUpd.getNombreProducto())
                                .precioEstimado(prodUpd.getPrecioEstimado())
                                .categoria(prodUpd.getCategoria())
                                .build())
                        .build()
                        , HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build()
                        , HttpStatus.NOT_FOUND);
            }
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build()
                    , HttpStatus.METHOD_NOT_ALLOWED);
        }
    }

     /*Metodod para Eliminar */
     @DeleteMapping("/deleteProd/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Optional<Producto> productDelete = iProductService.getProductById(id);

            if (productDelete.isEmpty()) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta eliminar no se encuentra en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }

            iProductService.deleteById(id);

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El producto ha sido eliminado correctamente.")
                            .object(null)
                            .build(),
                    HttpStatus.NO_CONTENT);
        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje(exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.METHOD_NOT_ALLOWED);
        }
    }




}
