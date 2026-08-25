package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.NominaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.NominaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.NominaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Nomina. Es informacion operativa del dia a dia -- ADMIN y USER
 * pueden crear y editar por igual. Borrar queda restringido solo a
 * ROLE_ADMIN por ser una accion mas sensible sobre datos financieros.
 * La lectura queda abierta a cualquier usuario autenticado, cubierta ya
 * por la regla general de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/nominas")
@RequiredArgsConstructor
public class NominaController {

    private final NominaService nominaService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<NominaDTO>> findAll() {
        return ResponseEntity.ok(nominaService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<NominaDTO> findById(@PathVariable Long id) {
        return ResponseEntity.ok(nominaService.findById(id));
    }

    /** Crea un registro nuevo. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<NominaDTO> create(@Valid @RequestBody NominaRequestDTO dto) {
        NominaDTO creado = nominaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<NominaDTO> update(@PathVariable Long id,
                                              @Valid @RequestBody NominaRequestDTO dto) {
        return ResponseEntity.ok(nominaService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        nominaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
