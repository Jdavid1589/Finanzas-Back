package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.GastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.GastoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.GastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Gasto. Es informacion operativa del dia a dia -- ADMIN y USER
 * pueden crear y editar por igual. Borrar queda restringido solo a
 * ROLE_ADMIN por ser una accion mas sensible sobre datos financieros.
 * La lectura queda abierta a cualquier usuario autenticado, cubierta ya
 * por la regla general de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/gastos")
@RequiredArgsConstructor
public class GastoController {

    private final GastoService gastoService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<GastoDTO>> findAll() {
        return ResponseEntity.ok(gastoService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<GastoDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(gastoService.findById(id));
    }

    /** Crea un registro nuevo. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<GastoDTO> create(@Valid @RequestBody GastoRequestDTO dto) {
        GastoDTO creado = gastoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<GastoDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody GastoRequestDTO dto) {
        return ResponseEntity.ok(gastoService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gastoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
