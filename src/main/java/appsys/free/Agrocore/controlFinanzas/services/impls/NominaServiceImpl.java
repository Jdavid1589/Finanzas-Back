package appsys.free.Agrocore.controlFinanzas.services.impls;

import appsys.free.Agrocore.controlFinanzas.entities.Cosecha;
import appsys.free.Agrocore.controlFinanzas.entities.Empleado;
import appsys.free.Agrocore.controlFinanzas.entities.Nomina;
import appsys.free.Agrocore.controlFinanzas.dtos.NominaDTO;
import appsys.free.Agrocore.controlFinanzas.dtos.NominaRequestDTO;
import appsys.free.Agrocore.controlFinanzas.mappers.NominaMapper;
import appsys.free.Agrocore.controlFinanzas.repositories.CosechaRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.EmpleadoRepository;
import appsys.free.Agrocore.controlFinanzas.repositories.NominaRepository;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.CosechaService;
import appsys.free.Agrocore.controlFinanzas.services.interfaces.NominaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NominaServiceImpl implements NominaService {

    private final NominaRepository nominaRepository;
    private final CosechaRepository cosechaRepository;
    private final EmpleadoRepository empleadoRepository;
    private final NominaMapper nominaMapper;
    private final CosechaService cosechaService;

    @Override
    @Transactional(readOnly = true)
    public List<NominaDTO> findAll() {
        return nominaMapper.toDtoList(nominaRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public NominaDTO findById(Long id) {
        return nominaMapper.toDto(buscarOFallar(id));
    }

    @Override
    @Transactional
    public NominaDTO create(NominaRequestDTO dto) {
        Nomina nomina = nominaMapper.toEntity(dto);
        nomina.setCosecha(buscarCosecha(dto.cosechaId()));
        nomina.setEmpleado(buscarEmpleado(dto.empleadoId()));
        calcularTotales(nomina, dto);

        Nomina guardada = nominaRepository.save(nomina);
        cosechaService.recalcularTotales(dto.cosechaId());

        return nominaMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public NominaDTO update(Long id, NominaRequestDTO dto) {
        Nomina nomina = buscarOFallar(id);
        Integer cosechaAnteriorId = nomina.getCosecha() != null ? nomina.getCosecha().getId() : null;

        nomina.setAnio(dto.anio());
        nomina.setSemana(dto.semana());
        nomina.setDias(dto.dias());
        nomina.setHoras(dto.horas());
        nomina.setMinutos(dto.minutos());
        nomina.setValorDia(dto.valorDia());
        nomina.setValorHora(dto.valorHora());
        nomina.setValorSemana(dto.valorSemana());
        nomina.setCantidadTipoGranel(dto.cantidadTipoGranel());
        nomina.setValorTipoGranel(dto.valorTipoGranel());
        nomina.setEstadoPago(dto.estadoPago());
        nomina.setCosecha(buscarCosecha(dto.cosechaId()));
        nomina.setEmpleado(buscarEmpleado(dto.empleadoId()));
        calcularTotales(nomina, dto);

        Nomina guardada = nominaRepository.save(nomina);

        cosechaService.recalcularTotales(dto.cosechaId());
        if (cosechaAnteriorId != null && !cosechaAnteriorId.equals(dto.cosechaId())) {
            cosechaService.recalcularTotales(cosechaAnteriorId);
        }

        return nominaMapper.toDto(guardada);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Nomina nomina = buscarOFallar(id);
        Integer cosechaId = nomina.getCosecha() != null ? nomina.getCosecha().getId() : null;

        nominaRepository.delete(nomina);

        if (cosechaId != null) {
            cosechaService.recalcularTotales(cosechaId);
        }
    }

    // Los campos "total_*" no vienen del cliente (no estan en NominaRequestDTO):
    // se derivan aqui de dias/horas/tipo_granel * sus valores unitarios.
    //
    // OJO: esta es una formula razonable segun los nombres de columna del
    // schema original, pero NO la confirme contra tu regla de negocio real.
    // Ajustala aqui si tu calculo real es otro.
    private void calcularTotales(Nomina nomina, NominaRequestDTO dto) {
        int dias = dto.dias() != null ? dto.dias() : 0;
        int valorDia = dto.valorDia() != null ? dto.valorDia() : 0;
        BigDecimal horas = dto.horas() != null ? dto.horas() : BigDecimal.ZERO;
        int valorHora = dto.valorHora() != null ? dto.valorHora() : 0;
        BigDecimal cantidadGranel = dto.cantidadTipoGranel() != null ? dto.cantidadTipoGranel() : BigDecimal.ZERO;
        int valorGranel = dto.valorTipoGranel() != null ? dto.valorTipoGranel() : 0;

        int totalDias = dias * valorDia;
        int totalHoras = horas.multiply(BigDecimal.valueOf(valorHora)).intValue();
        int totalGranel = cantidadGranel.multiply(BigDecimal.valueOf(valorGranel)).intValue();

        nomina.setTotalNominaDias(totalDias);
        nomina.setTotalNominaHora(totalHoras);
        nomina.setTotalNominaTipoGranel(totalGranel);
        nomina.setTotalNomina(totalDias + totalHoras + totalGranel);
    }

    private Nomina buscarOFallar(Long id) {
        return nominaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Nomina no encontrada con id " + id));
    }

    private Cosecha buscarCosecha(Integer id) {
        return cosechaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cosecha no encontrada con id " + id));
    }

    private Empleado buscarEmpleado(Integer id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Empleado no encontrado con id " + id));
    }
}
