package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGastoRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.TipoGastoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de TipoGasto. Es informacion de configuracion/catalogo, no operativa
 * del dia a dia -- por eso TODA escritura (crear, editar, borrar) esta
 * restringida a ROLE_ADMIN. La lectura queda abierta a cualquier usuario
 * autenticado (ADMIN o USER), sin anotacion adicional, porque ya la cubre
 * la regla general "anyRequest().authenticated()" de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/tipos-gasto")
@RequiredArgsConstructor
public class TipoGastoController {

    private final TipoGastoService tipoGastoService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<TipoGastoDTO>> findAll() {
        return ResponseEntity.ok(tipoGastoService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<TipoGastoDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoGastoService.findById(id));
    }

    /** Crea un registro nuevo. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TipoGastoDTO> create(@Valid @RequestBody TipoGastoRequestDTO dto) {
        TipoGastoDTO creado = tipoGastoService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoGastoDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody TipoGastoRequestDTO dto) {
        return ResponseEntity.ok(tipoGastoService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoGastoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
