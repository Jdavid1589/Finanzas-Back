package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.EmpleadoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.EmpleadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de Empleado. Es informacion operativa del dia a dia -- ADMIN y USER
 * pueden crear y editar por igual. Borrar queda restringido solo a
 * ROLE_ADMIN por ser una accion mas sensible sobre datos financieros.
 * La lectura queda abierta a cualquier usuario autenticado, cubierta ya
 * por la regla general de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/empleados")
@RequiredArgsConstructor
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<EmpleadoDTO>> findAll() {
        return ResponseEntity.ok(empleadoService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(empleadoService.findById(id));
    }

    /** Crea un registro nuevo. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PostMapping
    public ResponseEntity<EmpleadoDTO> create(@Valid @RequestBody EmpleadoRequestDTO dto) {
        EmpleadoDTO creado = empleadoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. ROLE_ADMIN o ROLE_USER. */
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @PutMapping("/{id}")
    public ResponseEntity<EmpleadoDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody EmpleadoRequestDTO dto) {
        return ResponseEntity.ok(empleadoService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        empleadoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
