package appsys.free.Finanzas.controlFinanzas.controllers;


import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.Productos_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.ReporteIngresoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import appsys.free.Finanzas.controlFinanzas.entities.Producto;
import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IIngresosService;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IReportesFinancieros;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ingresos")
@CrossOrigin(origins = "*")
public class IngresosController {

    private static final Logger logger = LoggerFactory.getLogger(IngresosController.class);
    @Autowired
    IIngresosService iIngresosService;
    @Autowired
    IReportesFinancieros iReportesService;

    /* list Id */
    @GetMapping("/listId/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Ingresos> ingresoOpt = iIngresosService.getIngresoById(id);

        if (ingresoOpt.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El registro que intenta buscar, no existe!!")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        Ingresos ingresos = ingresoOpt.get();

        // Construir el DTO a partir de la entidad
        Ingreso_Dto ingresoDto = Ingreso_Dto.builder()
                .id(ingresos.getId())
                .fechaIngreso(ingresos.getFechaIngreso())
                .descripcion(ingresos.getDescripcion())
                .cantidad(ingresos.getCantidad())
                .categoriaIngresos(ingresos.getCategIngresos())
                .build();

        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Consulta exitosa")
                        .object(ingresoDto)
                        .build(),
                HttpStatus.OK);
    }

    /* list All */
    @GetMapping("/listIngreso")
    public ResponseEntity<?> showAll() {
        List<Ingresos> ingresosList = iIngresosService.getAllIngresos();

        // Verifica si la lista obtenida está vacía (null normalmente no, salvo error de implementación)
        if (ingresosList == null || ingresosList.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No existen registros de ingresos.")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        // Convierte la lista de Ingresos a una lista de DTOs
        List<Ingreso_Dto> ingresoDtoList = ingresosList.stream()
                // El método 'map' transforma cada entidad en un DTO.
                .map(ingresos -> Ingreso_Dto.builder()
                        .id(ingresos.getId())
                        .fechaIngreso(ingresos.getFechaIngreso())
                        .descripcion(ingresos.getDescripcion())
                        .cantidad(ingresos.getCantidad())
                        .categoriaIngresos(ingresos.getCategIngresos())
                        .build())
                .toList(); // Convierte el Stream de DTOs en una List<Ingreso_Dto>

        // Devuelve una respuesta con la lista de Ingresos
        return new ResponseEntity<>(
                MensajeResponse.builder()
                        .mensaje("Consulta exitosa")
                        .object(ingresoDtoList)
                        .build(),
                HttpStatus.OK);
    }


    /* Obtener reporte de ingresos por mes y categoría */
    @GetMapping("/ingresos-mensual-categoria")
    public ResponseEntity<MensajeResponse> getReporteIngresosMensualCategoria() {
        try {
            List<ReporteIngresoDTO> reporte = iReportesService.getBalanceMensual_Dto();

            if (reporte.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(MensajeResponse.builder()
                                .mensaje("No existen registros de ingresos para generar el reporte.")
                                .object(Collections.emptyList())
                                .build());
            }

            return ResponseEntity.ok()
                    .body(MensajeResponse.builder()
                            .mensaje("Reporte de ingresos generado exitosamente")
                            .object(reporte)
                            .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MensajeResponse.builder()
                            .mensaje("Error al generar el reporte: " + e.getMessage())
                            .object(null)
                            .build());
        }
    }



    /* Add */
    @PostMapping("/addIngreso")
    public ResponseEntity<?> create(@RequestBody Ingreso_Dto ingresoDto) {
        try {
            // El service debe encargarse de convertir el DTO a entidad y guardarlo
            Ingresos ingresoGuardado = iIngresosService.save(ingresoDto);

            // Convierte la entidad guardada de nuevo a DTO para la respuesta
            Ingreso_Dto respuestaDto = Ingreso_Dto.builder()
                    .id(ingresoGuardado.getId())
                    .fechaIngreso(ingresoGuardado.getFechaIngreso())
                    .categoriaIngresos(ingresoGuardado.getCategIngresos())
                    .descripcion(ingresoGuardado.getDescripcion())
                    .cantidad(ingresoGuardado.getCantidad())
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

    /* Update */
    @PutMapping("/updIngreso/{id}")
    public ResponseEntity<?> update(@RequestBody Ingreso_Dto ingreso_dto, @PathVariable Long id) {
        try {
            if (!iIngresosService.existsById(id)) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta actualizar no se encuentra en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }

            // Asegúrate de que el ID del DTO sea el correcto
            ingreso_dto.setId(id);

            // Actualiza (el service puede verificar si es update o create según el ID)
            Ingresos ingresoUpdate = iIngresosService.save(ingreso_dto);

            // Mapear la entidad actualizada a DTO para la respuesta
            Ingreso_Dto respuestaDto = Ingreso_Dto.builder()
                    .id(ingresoUpdate.getId())
                    .fechaIngreso(ingresoUpdate.getFechaIngreso())
                    .descripcion(ingresoUpdate.getDescripcion())
                    .cantidad(ingresoUpdate.getCantidad())
                    .categoriaIngresos(ingresoUpdate.getCategIngresos())
                    .build();

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Actualizado correctamente")
                            .object(respuestaDto)
                            .build(),
                    HttpStatus.OK);

        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error al actualizar: " + exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

     /* Delete */
     @DeleteMapping("/deleteIngreso/{id}")
     public ResponseEntity<?> delete(@PathVariable Long id) {
         try {
             Optional<Ingresos> ingresoDelete = iIngresosService.getIngresoById(id);

             if (ingresoDelete.isEmpty()) {
                 return new ResponseEntity<>(
                         MensajeResponse.builder()
                                 .mensaje("El registro que intenta eliminar no se encuentra en la base de datos.")
                                 .object(null)
                                 .build(),
                         HttpStatus.NOT_FOUND);
             }

             iIngresosService.deleteById(id);

             // Si decides retornar un mensaje, usa HttpStatus.OK (200)
             return new ResponseEntity<>(
                     MensajeResponse.builder()
                             .mensaje("El ingreso ha sido eliminado correctamente.")
                             .object(null)
                             .build(),
                     HttpStatus.OK);

             // Si decides no retornar nada, puedes usar:
             // return ResponseEntity.noContent().build();

         } catch (DataAccessException exDt) {
             return new ResponseEntity<>(
                     MensajeResponse.builder()
                             .mensaje("Error al eliminar: " + exDt.getMessage())
                             .object(null)
                             .build(),
                     HttpStatus.INTERNAL_SERVER_ERROR);
         }
     }




}
