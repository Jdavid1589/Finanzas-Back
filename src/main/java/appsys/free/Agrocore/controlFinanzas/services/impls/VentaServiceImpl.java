package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Cliente;
import appsys.free.Agrocore.controlFinanzas.entities.Cosecha;
import appsys.free.Agrocore.controlFinanzas.entities.Venta;
import appsys.free.Agrocore.controlFinanzas.dtos.VentaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.VentaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.VentaMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.ClienteRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.CosechaRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.VentaRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.CosechaService;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final CosechaRepository cosechaRepository;
    private final ClienteRepository clienteRepository;
    private final VentaMapper ventaMapper;
    private final CosechaService cosechaService;

    @Override
    @Transactional(readOnly = true)
    public List<VentaDTO> findAll() {
        return ventaMapper.toDtoList(ventaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public VentaDTO findById(Long id) {
        return ventaMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public VentaDTO create(VentaRequestDTO dto) {
        Venta venta = ventaMapper.toEntity(dto);
        venta.setCosecha(buscarCosecha(dto.cosechaId()));
        venta.setCliente(buscarCliente(dto.clienteId()));
        // valorTotal no viene en el DTO: se calcula aqui, nunca lo manda el cliente.
        venta.setValorTotal(dto.cantidad() * dto.valorUnidad());

        Venta guardada = ventaRepository.save(venta);
        cosechaService.recalcularTotales(dto.cosechaId());

        return ventaMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public VentaDTO update(Long id, VentaRequestDTO dto) {
        Venta venta = buscarOFallar(id);
        Integer cosechaAnteriorId = venta.getCosecha() != null ? venta.getCosecha().getId() : null;

        venta.setCantidad(dto.cantidad());
        venta.setValorUnidad(dto.valorUnidad());
        venta.setValorTotal(dto.cantidad() * dto.valorUnidad());
        venta.setFecha(dto.fecha());
        venta.setCosecha(buscarCosecha(dto.cosechaId()));
        venta.setCliente(buscarCliente(dto.clienteId()));

        Venta guardada = ventaRepository.save(venta);

        cosechaService.recalcularTotales(dto.cosechaId());
        if (cosechaAnteriorId != null && !cosechaAnteriorId.equals(dto.cosechaId())) {
            cosechaService.recalcularTotales(cosechaAnteriorId);
        }

        return ventaMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Venta venta = buscarOFallar(id);
        Integer cosechaId = venta.getCosecha() != null ? venta.getCosecha().getId() : null;

        ventaRepository.delete(venta);

        if (cosechaId != null) {
            cosechaService.recalcularTotales(cosechaId);
        }
    }

    private Venta buscarOFallar(Long id) {
        return ventaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Venta no encontrada con id " + id));
    }

    private Cosecha buscarCosecha(Integer id) {
        return cosechaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cosecha no encontrada con id " + id));
    }

    private Cliente buscarCliente(Integer id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cliente no encontrado con id " + id));
    }
}
