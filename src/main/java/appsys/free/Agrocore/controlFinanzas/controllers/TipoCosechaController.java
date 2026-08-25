package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoCosechaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.TipoCosechaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de TipoCosecha. Es informacion de configuracion/catalogo, no operativa
 * del dia a dia -- por eso TODA escritura (crear, editar, borrar) esta
 * restringida a ROLE_ADMIN. La lectura queda abierta a cualquier usuario
 * autenticado (ADMIN o USER), sin anotacion adicional, porque ya la cubre
 * la regla general "anyRequest().authenticated()" de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/tipos-cosecha")
@RequiredArgsConstructor
public class TipoCosechaController {

    private final TipoCosechaService tipoCosechaService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<TipoCosechaDTO>> findAll() {
        return ResponseEntity.ok(tipoCosechaService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<TipoCosechaDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoCosechaService.findById(id));
    }

    /** Crea un registro nuevo. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TipoCosechaDTO> create(@Valid @RequestBody TipoCosechaRequestDTO dto) {
        TipoCosechaDTO creado = tipoCosechaService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoCosechaDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody TipoCosechaRequestDTO dto) {
        return ResponseEntity.ok(tipoCosechaService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoCosechaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
