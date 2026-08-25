package appsys.free.Agrocore.controlFinanzas.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

// OJO: a proposito NO incluye los campos "total_*" ni "utilidad" que si
// estan en CosechaDTO (respuesta). Esos son resultado de sumar sus gastos,
// nomina y ventas -- el cliente no deberia poder "inventarselos" al crear o
// editar una cosecha; el service los recalcula. Si tu app actual los
// captura manualmente en el formulario, es una senal de que conviene mover
// ese calculo al backend mas adelante (ver sugerencias que ya te di).
public record CosechaRequestDTO(
        @NotBlank(message = "El nombre de la cosecha es obligatorio")
        String nombre,

        @NotNull(message = "El tipo de cosecha es obligatorio")
        Integer tipoCosechaId,

        @NotNull(message = "El tipo de granel es obligatorio")
        Integer tipoGranelId,

        @NotNull(message = "La fecha de inicio es obligatoria")
        LocalDate fechaInicio,

        // Nula mientras la cosecha sigue activa
        LocalDate fechaFinal
) {}
