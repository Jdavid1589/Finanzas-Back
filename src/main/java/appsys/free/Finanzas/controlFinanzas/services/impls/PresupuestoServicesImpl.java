package appsys.free.Finanzas.controlFinanzas.services.impls;

import appsys.free.Finanzas.controlFinanzas.dtos.Gastos_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.Ingreso_Dto;
import appsys.free.Finanzas.controlFinanzas.dtos.PresupuestoDto;
import appsys.free.Finanzas.controlFinanzas.dtos.SaldoPresupuestoDTO;
import appsys.free.Finanzas.controlFinanzas.entities.CategoriaGastos;
import appsys.free.Finanzas.controlFinanzas.entities.Gastos;
import appsys.free.Finanzas.controlFinanzas.entities.Ingresos;
import appsys.free.Finanzas.controlFinanzas.entities.Presupuesto;
import appsys.free.Finanzas.controlFinanzas.repositories.ICategoriaRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IIngresoRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IPresupuestoRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IIngresosService;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IPresupuestoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PresupuestoServicesImpl implements IPresupuestoService {

    private static final Logger logger = LoggerFactory.getLogger(PresupuestoServicesImpl.class);

    @Autowired
    private IPresupuestoRepo presupuestoRepo;

    @Autowired
    private ICategoriaRepo categoriaRepo;

    //--------------------------------------------------

    //Guardar Presupuesto
    @Override
    public Presupuesto save(PresupuestoDto presupuestoDto) {
        Presupuesto presupuesto;
        if (presupuestoDto.getId() != null && presupuestoRepo.existsById(presupuestoDto.getId())) {
            // Update
            presupuesto = presupuestoRepo.findById(presupuestoDto.getId()).orElseThrow();
            presupuesto.setFecha(presupuestoDto.getFecha());
            presupuesto.setMonto(presupuestoDto.getMonto());
            presupuesto.setCategoriaGastos(presupuestoDto.getCategoriaGastos());
            presupuesto.setDescripcion(presupuestoDto.getDescripcion());
        } else {
            // Create
            presupuesto = Presupuesto.builder()
                    .fecha(presupuestoDto.getFecha())
                    .monto(presupuestoDto.getMonto())
                    .categoriaGastos(presupuestoDto.getCategoriaGastos())
                    .descripcion(presupuestoDto.getDescripcion())
                    .build();
        }
        return presupuestoRepo.save(presupuesto);
    }

    /*---------------------------------------------------*/

    //Validar si existe Presupuesto
    @Override
    public boolean existsById(Long id) {
        return presupuestoRepo.existsById(id);
    }

    /*---------------------------------------------------*/

    // 🔹 Obtener Presupuesto por ID
    @Override
    public Optional<Presupuesto> getPresupById(Long id) {
        return presupuestoRepo.findById(id);
    }

    /*---------------------------------------------------*/

    // 🔹 Eliminar por ID
    @Override
    public void deleteById(Long id) {
        presupuestoRepo.deleteById(id);
    }

    /*---------------------------------------------------*/

    // 🔹 Listar Todos los presupuestos
    @Override
    public List<Presupuesto> getAllPresupuesto() {
        return presupuestoRepo.findAll();
    }

    /*---------------------------------------------------*/

    // 🔹 Eliminar presupuestos 2
    @Override
    public void eliminarPresupuesto(Long id) {
        if (!presupuestoRepo.existsById(id)) {
            throw new RuntimeException("No existe presupuesto con id: " + id);
        }
        presupuestoRepo.deleteById(id);
        logger.info("Presupuesto eliminado con id {}", id);
    }

    /*---------------------------------------------------*/

    // 🔹 Listar Presupuesto por ID
    @Override
    public PresupuestoDto obtenerPresupuesto(Long id) {
        return presupuestoRepo.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado con id: " + id));
    }

    /*---------------------------------------------------*/

   // 🔹 Listar Presupuesto All
    @Override
    public List<PresupuestoDto> listarPresupuestos() {
        return presupuestoRepo.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    /*---------------------------------------------------*/

    // 🔹 Ahora ya devuelve DTO directo
    @Override
    public List<SaldoPresupuestoDTO> getPresupuestoPorCategoria() {
        return presupuestoRepo.getPresupuestoPorCategoria();
    }

    // 🔹 Ahora ya devuelve DTO directo
    @Override
    public List<SaldoPresupuestoDTO> getPresupuestoPorMes() {
        return presupuestoRepo.getPresupuestoPorMes();
    }

    // 🔹 En Uso Presupuesto Por Mes Y Categoria (2)
    @Override
    public List<SaldoPresupuestoDTO> getPresupuestoPorMesYCategoria() {
        return presupuestoRepo.getPresupuestoPorMesYCategoria2();
    }

    private PresupuestoDto mapToDTO(Presupuesto presupuesto) {
        return new PresupuestoDto(
                presupuesto.getId(),
                presupuesto.getFecha(),
                presupuesto.getMonto(),
                presupuesto.getDescripcion(),
                presupuesto.getCategoriaGastos()
        );
    }










}


