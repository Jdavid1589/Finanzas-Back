package appsys.free.Finanzas.controlFinanzas.controllers;



import appsys.free.Finanzas.controlFinanzas.dtos.Gastos_Dto;
import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IGastosService;
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
@RequestMapping("/api/gastos")
@CrossOrigin(origins = "*")
public class GastosController {

    private static final Logger logger = LoggerFactory.getLogger(GastosController.class);
    @Autowired
    IGastosService iGastosService;

    /* Obtener por ID */
    @GetMapping("/listId/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Gastos> gastoOpt = iGastosService.getGastosById(id);

        if (gastoOpt.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("El gasto que intenta buscar no existe.")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND);
        }

        Gastos gastos = gastoOpt.get();

        Gastos_Dto gastoDto = Gastos_Dto.builder()
                .id(gastos.getId())
                .fechaGasto(gastos.getFechaGasto())
                .categoriaGastos(gastos.getCategGastos())
                .montoGasto(gastos.getMontoGasto())
                .build();

        return ResponseEntity.ok(
                MensajeResponse.builder()
                        .mensaje("Consulta exitosa")
                        .object(gastoDto)
                        .build()
        );
    }

    /* Obtener todos */
    @GetMapping("/listGastos")
    public ResponseEntity<?> showAll() {
        List<Gastos> gastosList = iGastosService.getAllGastos();

        if (gastosList.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No existen registros de gastos.")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }

        List<Gastos_Dto> gastosDtoList = gastosList.stream()
                .map(g -> Gastos_Dto.builder()
                        .id(g.getId())
                        .fechaGasto(g.getFechaGasto())
                        .montoGasto(g.getMontoGasto())
                        .categoriaGastos(g.getCategGastos()) // 🔑 se manda el objeto completo
                        .build())
                .toList();

        return ResponseEntity.ok(
                MensajeResponse.builder()
                        .mensaje("Consulta exitosa")
                        .object(gastosDtoList)
                        .build()
        );
    }



    /* Crear */
    @PostMapping("/addGastos")
    public ResponseEntity<?> create(@RequestBody Gastos_Dto gastos_dto) {
        try {
            Gastos gastoSave = iGastosService.save(gastos_dto);

            Gastos_Dto respuestaDto = Gastos_Dto.builder()
                    .id(gastoSave.getId())
                    .fechaGasto(gastoSave.getFechaGasto())
                    .montoGasto(gastoSave.getMontoGasto())
                    .categoriaGastos(gastoSave.getCategGastos())
                    .build();

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Gasto guardado correctamente")
                            .object(respuestaDto)
                            .build(),
                    HttpStatus.CREATED);

        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(
                    MensajeResponse.builder()
                            .mensaje("Error: " + ex.getMessage())
                            .object(null)
                            .build()
            );
        } catch (Exception ex) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error interno del servidor: " + ex.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Actualizar */
    @PutMapping("/updGastos/{id}")
    public ResponseEntity<?> update(@RequestBody Gastos_Dto gastos_dto, @PathVariable Long id) {
        try {
            if (!iGastosService.existsById(id)) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El gasto que intenta actualizar no existe en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }

            gastos_dto.setId(id);
            Gastos gastoSave = iGastosService.save(gastos_dto);

            Gastos_Dto respuestaDto = Gastos_Dto.builder()
                    .id(gastoSave.getId())
                    .fechaGasto(gastoSave.getFechaGasto())
                    .montoGasto(gastoSave.getMontoGasto())
                    .categoriaGastos(gastoSave.getCategGastos())
                    .build();

            return ResponseEntity.ok(
                    MensajeResponse.builder()
                            .mensaje("Gasto actualizado correctamente")
                            .object(respuestaDto)
                            .build()
            );

        } catch (DataAccessException exDt) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Error al actualizar: " + exDt.getMessage())
                            .object(null)
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* Eliminar */
    @DeleteMapping("/deleteGastos/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Optional<Gastos> gastosDelete = iGastosService.getGastosById(id);

            if (gastosDelete.isEmpty()) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El gasto que intenta eliminar no existe en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }

            iGastosService.deleteById(id);

            return ResponseEntity.ok(
                    MensajeResponse.builder()
                            .mensaje("El gasto ha sido eliminado correctamente.")
                            .object(null)
                            .build()
            );

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
