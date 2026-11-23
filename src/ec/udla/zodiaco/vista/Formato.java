package ec.udla.zodiaco.vista;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public final class Formato {
    private static final DecimalFormat MONEDA;

    static {
        DecimalFormatSymbols sym = new DecimalFormatSymbols(Locale.US);
        MONEDA = new DecimalFormat("#,##0.00", sym);
    }

    private Formato() {}

    public static String moneda(BigDecimal valor) {
        return valor == null ? "0.00" : MONEDA.format(valor);
    }
}
