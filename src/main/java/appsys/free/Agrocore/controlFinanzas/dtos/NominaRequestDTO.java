package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;

// Igual que en Cosecha: los "total_*" no vienen del cliente, se calculan en
// el service a partir de dias/horas/valor_dia/valor_hora, etc.
public record NominaRequestDTO(
        @NotNull(message = "La cosecha es obligatoria")
        Integer cosechaId,

        @NotNull(message = "El empleado es obligatorio")
        Integer empleadoId,

        @NotNull Integer anio,
        @NotNull Integer semana,

        @PositiveOrZero Integer dias,
        BigDecimal horas,
        Integer minutos,

        Integer valorDia,
        Integer valorHora,
        Integer valorSemana,

        BigDecimal cantidadTipoGranel,
        Integer valorTipoGranel,

        Boolean estadoPago
) {}
