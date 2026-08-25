package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.CambiarEstadoCosechaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.CosechaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.CosechaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Cosecha. Es informacion operativa del dia a dia -- ADMIN y USER
 * pueden crear y editar por igual. Borrar queda restringido solo a
 * ROLE_ADMIN por ser una accion mas sensible sobre datos financieros.
 * La lectura queda abierta a cualquier usuario autenticado, cubierta ya
 * por la regla general de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/cosechas")
@RequiredArgsConstructor
public class CosechaController {

    private final CosechaService cosechaService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<CosechaDTO>> findAll() {
        return ResponseEntity.ok(cosechaService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<CosechaDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(cosechaService.findById(id));
    }

    /** Crea un registro nuevo. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<CosechaDTO> create(@Valid @RequestBody CosechaRequestDTO dto) {
        CosechaDTO creado = cosechaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<CosechaDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody CosechaRequestDTO dto) {
        return ResponseEntity.ok(cosechaService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        cosechaService.delete(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Fuerza el recalculo de los totales de una cosecha desde cero, sumando
     * de nuevo todos sus gastos/nomina/ventas actuales. Util para:
     * - Corregir cosechas cuyos gastos/ventas se crearon con una version
     *   anterior del codigo (antes de que existiera este recalculo automatico).
     * - Reconciliar datos si sospechas que algo quedo desincronizado.
     * <p>
     * En el flujo normal NUNCA deberias necesitar llamarlo: crear/editar/
     * borrar un Gasto, Nomina o Venta ya dispara este mismo recalculo solo.
     * Restringido a ADMIN por ser una operacion de mantenimiento, no de uso diario.
     */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/{id}/recalcular-totales")
    public ResponseEntity<CosechaDTO> recalcularTotales(@PathVariable Integer id) {
        cosechaService.recalcularTotales(id);
        return ResponseEntity.ok(cosechaService.findById(id));
    }

    /**
     * Cambia el estado de una cosecha (ej. de ACTIVA a LIQUIDADA). Endpoint
     * dedicado, separado del PUT general: cambiar el estado es una accion
     * de negocio con reglas propias (transiciones validas), no un campo
     * mas para editar junto con nombre/fechas. ROLE_ADMIN o ROLE_USER,
     * igual que el resto de escritura operativa sobre Cosecha.
     */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PatchMapping("/{id}/estado")
    public ResponseEntity<CosechaDTO> cambiarEstado(@PathVariable Integer id,
                                                      @Valid @RequestBody CambiarEstadoCosechaRequestDTO dto) {
        return ResponseEntity.ok(cosechaService.cambiarEstado(id, dto.estado()));
    }

    /**
     * Reabrir una cosecha (ej. de ACTIVA a LIQUIDADA). Endpoint
    */
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}/reabrir")
    public ResponseEntity<CosechaDTO> reabrir(
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(
                cosechaService.reabrir(id)
        );
    }
}
