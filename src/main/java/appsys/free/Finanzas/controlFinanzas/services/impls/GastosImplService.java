package appsys.free.Finanzas.controlFinanzas.services.impls;


import appsys.free.Finanzas.controlFinanzas.dtos.Gastos_Dto;

import appsys.free.Finanzas.controlFinanzas.entities.Gastos;

import appsys.free.Finanzas.controlFinanzas.repositories.ICategoriaRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IGastosRepo;
import appsys.free.Finanzas.controlFinanzas.repositories.IPresupuestoRepo;
import appsys.free.Finanzas.controlFinanzas.services.interfaces.IGastosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GastosImplService implements IGastosService {

    private static final Logger logger = LoggerFactory.getLogger(GastosImplService.class);

    @Autowired
    IGastosRepo iGastosRepo;

    @Autowired
    private IGastosRepo gastosRepository;

    @Autowired
    private IPresupuestoRepo presupuestoRepository;
    @Autowired
    private ICategoriaRepo categoriaRepo;


    @Override
    public List<Gastos> getAllGastos() {
        return iGastosRepo.findAll();
    }

    /* Metodo para Listar con el Objeto Completo de Caterogiras */
  /*  public List<GastosResponseDTO> listar() {
        return iGastosRepo.findAll().stream()
                .map(g -> {
                    GastosResponseDTO dto = new GastosResponseDTO();
                    dto.setId(g.getId());
                    dto.setFechaGasto(g.getFechaGasto());
                    dto.setMontoGasto(g.getMontoGasto());
                    dto.setCategoriaGastos(g.getCategGastos()); // se envía el objeto
                    return dto;
                })
                .toList();
    }
*/


    @Override
    public Gastos save(Gastos_Dto gastos_dto) {
        Gastos gastos;
        if (gastos_dto.getId() != null && iGastosRepo.existsById(gastos_dto.getId())) {
            // Es update: recupera la entidad y actualiza campos
            gastos = iGastosRepo.findById(gastos_dto.getId()).get();
            gastos.setFechaGasto(gastos_dto.getFechaGasto());
            gastos.setMontoGasto(gastos_dto.getMontoGasto());
            gastos.setCategGastos(gastos_dto.getCategoriaGastos());
        } else {
            // Es create: crea nueva entidad
            gastos = Gastos.builder()
                    .fechaGasto(gastos_dto.getFechaGasto())
                    .montoGasto(gastos_dto.getMontoGasto())
                    .categGastos(gastos_dto.getCategoriaGastos())
                    .build();
        }
        return iGastosRepo.save(gastos);
    }


    @Override
    public Optional<Gastos> getGastosById(Long id) {
        return iGastosRepo.findById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return iGastosRepo.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        iGastosRepo.deleteById(id);
    }


}

   /* public Gastos crearGasto(Gastos gasto, Long presupuestoId) {
        Presupuesto presupuesto = presupuestoRepository.findById(presupuestoId)
                .orElseThrow(() -> new RuntimeException("Presupuesto no encontrado"));

        // Calcular gastos actuales en este presupuesto
        Integer gastosActuales = gastosRepository.sumGastosByPresupuesto(presupuestoId);

        // Validar que no exceda el presupuesto
        if (gastosActuales + gasto.getMontoGasto() > presupuesto.getMonto()) {
            throw new RuntimeException("El gasto excede el presupuesto disponible");
        }

        gasto.setPresupuesto(presupuesto);
        return gastosRepository.save(gasto);
    }
}*/
