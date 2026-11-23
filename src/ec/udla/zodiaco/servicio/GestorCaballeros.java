package ec.udla.zodiaco.servicio;

import ec.udla.zodiaco.modelo.Caballero;
import ec.udla.zodiaco.modelo.Rango;
import ec.udla.zodiaco.repositorio.RepositorioCaballeros;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorCaballeros {
    private final RepositorioCaballeros repositorio;
    private final CalculadoraFinanciera calculadora;

    public GestorCaballeros(RepositorioCaballeros repositorio) {
        this.repositorio = repositorio;
        this.calculadora = new CalculadoraFinanciera();
    }

    public CalculadoraFinanciera getCalculadora() {
        return calculadora;
    }

    public String getNombreEstructura() {
        return repositorio.nombreEstructura();
    }

    public Caballero registrar(int id, String nombre, Rango rango, String constelacion,
                               int poder, String mision, int dificultad, BigDecimal recompensa) {
        Caballero c = new Caballero(id, nombre, rango, constelacion, poder, mision, dificultad, recompensa);
        repositorio.registrar(c);
        return c;
    }

    public Caballero modificar(int id, String nombre, Rango rango, String constelacion,
                               int poder, String mision, int dificultad, BigDecimal recompensa) {
        Caballero c = new Caballero(id, nombre, rango, constelacion, poder, mision, dificultad, recompensa);
        repositorio.actualizar(c);
        return c;
    }

    public Optional<Caballero> buscar(int id) {
        return repositorio.buscarPorId(id);
    }

    public List<Caballero> listar() {
        return repositorio.listar();
    }

    public int cantidad() {
        return repositorio.cantidad();
    }

    public Informe generarInforme() {
        List<Informe.Fila> filas = new ArrayList<>();
        BigDecimal totRec = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totAp = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totImp = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);
        BigDecimal totNet = BigDecimal.ZERO.setScale(2, RoundingMode.HALF_UP);

        for (Caballero c : repositorio.listar()) {
            CalculadoraFinanciera.Resultado r = calculadora.calcular(c.getRecompensa());
            filas.add(new Informe.Fila(c, r.aporte(), r.impuesto(), r.neta()));
            totRec = totRec.add(r.recompensa());
            totAp = totAp.add(r.aporte());
            totImp = totImp.add(r.impuesto());
            totNet = totNet.add(r.neta());
        }
        return new Informe(filas, totRec, totAp, totImp, totNet);
    }

    public int cargarDatosDeEjemplo() {
        Object[][] ejemplos = {
            {1, "Seiya", Rango.BRONCE, "Pegaso", 8, "Rescatar a Atena en el Templo Sur", 3, new BigDecimal("85000")},
            {2, "Shiryu", Rango.BRONCE, "Dragón", 8, "Custodiar las Cinco Montañas de Rozan", 3, new BigDecimal("150000")},
            {3, "Hyoga", Rango.BRONCE, "Cisne", 7, "Patrullar la frontera de Siberia", 2, new BigDecimal("100000")},
            {4, "Shun", Rango.BRONCE, "Andrómeda", 7, "Proteger la Isla de Andrómeda", 2, new BigDecimal("60000")},
            {5, "Ikki", Rango.BRONCE, "Fénix", 9, "Infiltrar la Isla de la Reina Muerte", 5, new BigDecimal("320000")},
            {6, "Misty", Rango.PLATA, "Lagarto", 6, "Vigilar el Muro de los Lamentos", 3, new BigDecimal("200000")},
            {7, "Aioria", Rango.ORO, "Leo", 9, "Defender la Quinta Casa del Zodiaco", 4, new BigDecimal("410000")},
            {8, "Shaka", Rango.ORO, "Virgo", 10, "Custodiar la Sexta Casa del Zodiaco", 5, new BigDecimal("520000")}
        };
        int cargados = 0;
        for (Object[] e : ejemplos) {
            int id = (int) e[0];
            if (!repositorio.existe(id)) {
                registrar(id, (String) e[1], (Rango) e[2], (String) e[3],
                          (int) e[4], (String) e[5], (int) e[6], (BigDecimal) e[7]);
                cargados++;
            }
        }
        return cargados;
    }
}
