package ec.udla.zodiaco.servicio;

import ec.udla.zodiaco.modelo.Caballero;
import java.math.BigDecimal;
import java.util.List;

public record Informe(
    List<Fila> filas,
    BigDecimal totalRecompensa,
    BigDecimal totalAporte,
    BigDecimal totalImpuesto,
    BigDecimal totalNeta
) {
    public record Fila(
        Caballero caballero,
        BigDecimal aporte,
        BigDecimal impuesto,
        BigDecimal neta
    ) {}
}
