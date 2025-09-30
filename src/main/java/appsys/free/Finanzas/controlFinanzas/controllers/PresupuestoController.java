package appsys.free.Finanzas.controlFinanzas.controllers;

import appsys.free.Finanzas.controlFinanzas.dtos.PresupuestoDto;
import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.Presupuesto;
import appsys.free.Finanzas.controlFinanzas.payload.MensajeResponse;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IPresupuestoService;
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
@RequestMapping("/api/presupuestos")
@CrossOrigin(origins = "*")
public class PresupuestoController {

    private static final Logger logger = LoggerFactory.getLogger(PresupuestoController.class);

    @Autowired
    private IPresupuestoService presupuestoService;

    /* Listar / ID */
    @GetMapping("listar/{id}")
    public ResponseEntity<PresupuestoDto> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(presupuestoService.obtenerPresupuesto(id));
    }

    /*Listar Presupuesto All*/
    @GetMapping("list-all")
    public ResponseEntity<List<PresupuestoDto>> listar() {
        return ResponseEntity.ok(presupuestoService.listarPresupuestos());
    }


    /* Obtener todos */
    @GetMapping("/listall")
    public ResponseEntity<?> showAll() {
        List<Presupuesto> presupList = presupuestoService.getAllPresupuesto();

        if (presupList.isEmpty()) {
            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("No existen registros de Presupuesto.")
                            .object(null)
                            .build(),
                    HttpStatus.NOT_FOUND
            );
        }

        List<PresupuestoDto> PresupDtoList = presupList.stream()
                .map(g -> PresupuestoDto.builder()
                        .id(g.getId())
                        .fecha(g.getFecha())
                        .monto(g.getMonto())
                        .categoriaGastos(g.getCategoriaGastos()) // 🔑 se manda el objeto completo
                        .descripcion(g.getDescripcion())
                        .build())
                .toList();

        return ResponseEntity.ok(
                MensajeResponse.builder()
                        .mensaje("Consulta exitosa")
                        .object(PresupDtoList)
                        .build()
        );
    }

    /* Crear */
    @PostMapping("/addPresupuesto")
    public ResponseEntity<?> create(@RequestBody PresupuestoDto presupuestoDto) {
        try {
            Presupuesto presupuestoSave = presupuestoService.save(presupuestoDto);

            PresupuestoDto respuestaDto = PresupuestoDto.builder()
                    .id(presupuestoSave.getId())
                    .fecha(presupuestoSave.getFecha())
                    .monto(presupuestoSave.getMonto())
                    .categoriaGastos(presupuestoSave.getCategoriaGastos())
                    .descripcion(presupuestoSave.getDescripcion())
                    .build();

            return new ResponseEntity<>(
                    MensajeResponse.builder()
                            .mensaje("Registro guardado correctamente")
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
    @PutMapping("/updPresupuesto/{id}")
    public ResponseEntity<?> update(@RequestBody PresupuestoDto presupuestoDto, @PathVariable Long id) {
        try {
            if (!presupuestoService.existsById(id)) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El Registro que intenta actualizar no existe en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }

            presupuestoDto.setId(id);
            Presupuesto presupSave = presupuestoService.save(presupuestoDto);

            PresupuestoDto respuestaDto = presupuestoDto.builder()
                    .id(presupSave.getId())
                    .fecha(presupSave.getFecha())
                    .monto(presupSave.getMonto())
                    .categoriaGastos(presupSave.getCategoriaGastos())
                    .descripcion(presupSave.getDescripcion())
                    .build();

            return ResponseEntity.ok(
                    MensajeResponse.builder()
                            .mensaje("Registro actualizado correctamente")
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
    @DeleteMapping("eliminar/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        presupuestoService.eliminarPresupuesto(id);
        return ResponseEntity.noContent().build();
    }



    /* Eliminar ok */
   @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            Optional<Presupuesto> presupDelete = presupuestoService.getPresupById(id);

            if (presupDelete.isEmpty()) {
                return new ResponseEntity<>(
                        MensajeResponse.builder()
                                .mensaje("El registro que intenta eliminar no existe en la base de datos.")
                                .object(null)
                                .build(),
                        HttpStatus.NOT_FOUND);
            }

            presupuestoService.deleteById(id);

            return ResponseEntity.ok(
                    MensajeResponse.builder()
                            .mensaje("El registro ha sido eliminado correctamente.")
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


/*----------------------------REPORTES OK--------------------------------------*/
    /* 📊 Reportes ajustados a DTO */

    // 🔹
    @GetMapping("/por-categoria")
    public ResponseEntity<List<SaldoPresupuestoDTO>> getPresupuestoPorCategoria() {
        return ResponseEntity.ok(presupuestoService.getPresupuestoPorCategoria());
    }

    // 🔹
    @GetMapping("/por-mes")
    public ResponseEntity<List<SaldoPresupuestoDTO>> getPresupuestoPorMes() {
        return ResponseEntity.ok(presupuestoService.getPresupuestoPorMes());
    }

    // 🔹
    @GetMapping("/por-mes-categ")
    public ResponseEntity<List<SaldoPresupuestoDTO>> getPresupuestoPorMesCatego() {
        return ResponseEntity.ok(presupuestoService.getPresupuestoPorMesYCategoria());
    }


}
