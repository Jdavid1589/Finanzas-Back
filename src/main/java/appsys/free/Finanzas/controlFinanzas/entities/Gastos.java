package appsys.free.Finanzas.controlFinanzas.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Entity
    public class Gastos {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Temporal(TemporalType.DATE)
        private Date fechaGasto;

        private BigDecimal montoGasto; // Cantidad

        @ManyToOne
        @JoinColumn(name = "categ_gastos_id")
        private CategoriaGastos categGastos;

        /*Constructor*/
        public Gastos(Long id) {
            this.id = id;
        }
    }
