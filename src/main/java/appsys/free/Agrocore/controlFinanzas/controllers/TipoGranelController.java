package appsys.free.Agrocore.controlFinanzas.controllers;

import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.TipoGranelRequestDTO;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.TipoGranelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * CRUD de TipoGranel. Es informacion de configuracion/catalogo, no operativa
 * del dia a dia -- por eso TODA escritura (crear, editar, borrar) esta
 * restringida a ROLE_ADMIN. La lectura queda abierta a cualquier usuario
 * autenticado (ADMIN o USER), sin anotacion adicional, porque ya la cubre
 * la regla general "anyRequest().authenticated()" de SpringSecurityConfig.
 */
@RestController
@RequestMapping("/api/tipos-granel")
@RequiredArgsConstructor
public class TipoGranelController {

    private final TipoGranelService tipoGranelService;

    /** Lista todos los registros. Cualquier usuario autenticado. */
    @GetMapping
    public ResponseEntity<List<TipoGranelDTO>> findAll() {
        return ResponseEntity.ok(tipoGranelService.findAll());
    }

    /** Busca un registro por id. Cualquier usuario autenticado. */
    @GetMapping("/{id}")
    public ResponseEntity<TipoGranelDTO> findById(@PathVariable Integer id) {
        return ResponseEntity.ok(tipoGranelService.findById(id));
    }

    /** Crea un registro nuevo. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<TipoGranelDTO> create(@Valid @RequestBody TipoGranelRequestDTO dto) {
        TipoGranelDTO creado = tipoGranelService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(creado);
    }

    /** Edita un registro existente. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<TipoGranelDTO> update(@PathVariable Integer id,
                                              @Valid @RequestBody TipoGranelRequestDTO dto) {
        return ResponseEntity.ok(tipoGranelService.update(id, dto));
    }

    /** Elimina un registro. Solo ROLE_ADMIN. */
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        tipoGranelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
