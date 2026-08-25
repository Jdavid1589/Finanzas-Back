package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.InsumoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.InsumoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.InsumoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Insumo. Es informacion operativa del dia a dia -- ADMIN y USER
 * pueden crear y editar por igual. Borrar queda restringido solo a
 * ROLE_ADMIN por ser una accion mas sensible sobre datos financieros.
 * La lectura queda abierta a cualquier usuario autenticado, cubierta ya
 * por la regla general de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/insumos")
@RequiredArgsConstructor
public class InsumoController {

    private final InsumoService insumoService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<InsumoDTO>> findAll() {
        return ResponseEntity.ok(insumoService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<InsumoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(insumoService.findById(id));
    }

    /** Crea un registro nuevo. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<InsumoDTO> create(@Valid @RequestBody InsumoRequestDTO dto) {
        InsumoDTO creado = insumoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<InsumoDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody InsumoRequestDTO dto) {
        return ResponseEntity.ok(insumoService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        insumoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
