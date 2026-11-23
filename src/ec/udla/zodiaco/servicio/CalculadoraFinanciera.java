package ec.udla.zodiaco.servicio;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CalculadoraFinanciera {

    public enum MetodoImpuesto {
        PROGRESIVO("Progresivo por tramos (acumulado)"),
        DIRECTO("Directo: tasa x exceso del último tramo");

        private final String descripcion;
        MetodoImpuesto(String descripcion) { this.descripcion = descripcion; }
        @Override public String toString() { return descripcion; }
    }

    private record Tramo(BigDecimal desde, BigDecimal hasta, BigDecimal tasa) {}

    public record Resultado(BigDecimal recompensa, BigDecimal aporte, BigDecimal impuesto, BigDecimal neta) {}

    private static final BigDecimal TASA_APORTE = new BigDecimal("0.10");

    private static final Tramo[] TRAMOS = {
        new Tramo(BigDecimal.ZERO, new BigDecimal("100000"), BigDecimal.ZERO),
        new Tramo(new BigDecimal("100000"), new BigDecimal("200000"), new BigDecimal("0.12")),
        new Tramo(new BigDecimal("200000"), new BigDecimal("400000"), new BigDecimal("0.25")),
        new Tramo(new BigDecimal("400000"), null, new BigDecimal("0.35"))
    };

    private MetodoImpuesto metodo = MetodoImpuesto.PROGRESIVO;

    public MetodoImpuesto getMetodo() { return metodo; }
    public void setMetodo(MetodoImpuesto metodo) {
        if (metodo != null) this.metodo = metodo;
    }

    public BigDecimal aporte(BigDecimal recompensa) {
        return recompensa.multiply(TASA_APORTE).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal impuesto(BigDecimal recompensa) {
        BigDecimal total = BigDecimal.ZERO;
        for (Tramo t : TRAMOS) {
            if (recompensa.compareTo(t.desde) <= 0) {
                break;
            }
            if (metodo == MetodoImpuesto.PROGRESIVO) {
                BigDecimal tope = (t.hasta == null || recompensa.compareTo(t.hasta) < 0) ? recompensa : t.hasta;
                BigDecimal baseTramo = tope.subtract(t.desde);
                total = total.add(baseTramo.multiply(t.tasa));
            } else {
                BigDecimal exceso = recompensa.subtract(t.desde);
                total = exceso.multiply(t.tasa);
            }
        }
        return total.setScale(2, RoundingMode.HALF_UP);
    }

    public Resultado calcular(BigDecimal recompensa) {
        BigDecimal rec = recompensa.setScale(2, RoundingMode.HALF_UP);
        BigDecimal ap = aporte(rec);
        BigDecimal imp = impuesto(rec);
        BigDecimal neta = rec.subtract(ap).subtract(imp).setScale(2, RoundingMode.HALF_UP);
        return new Resultado(rec, ap, imp, neta);
    }
}
