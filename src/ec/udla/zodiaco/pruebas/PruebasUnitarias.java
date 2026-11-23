package ec.udla.zodiaco.pruebas;

import ec.udla.zodiaco.modelo.Caballero;
import ec.udla.zodiaco.modelo.Rango;
import ec.udla.zodiaco.repositorio.RepositorioCaballeros;
import ec.udla.zodiaco.repositorio.RepositorioLista;
import ec.udla.zodiaco.repositorio.RepositorioMapa;
import ec.udla.zodiaco.servicio.CalculadoraFinanciera;
import ec.udla.zodiaco.servicio.GestorCaballeros;
import ec.udla.zodiaco.servicio.Informe;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

public class PruebasUnitarias {
    private static int aprobados = 0;
    private static int totales = 0;

    private static void verificar(String codigo, String desc, boolean condicion) {
        totales++;
        if (condicion) {
            aprobados++;
            System.out.printf("[%s] APROBADO  - %s%n", codigo, desc);
        } else {
            System.out.printf("[%s] FALLIDO   - %s%n", codigo, desc);
        }
    }

    private static void probarRepositorio(String sufijo, RepositorioCaballeros repo) {
        GestorCaballeros g = new GestorCaballeros(repo);
        Caballero c1 = g.registrar(1, "Seiya", Rango.BRONCE, "Pegaso", 8, "Templo Sur", 3, new BigDecimal("85000"));
        verificar("CP-22" + sufijo, "Registrar caballero válido [" + sufijo + "]", c1.getId() == 1 && g.cantidad() == 1);

        boolean dup = false;
        try {
            g.registrar(1, "Otro", Rango.BRONCE, "Pegaso", 5, "Misión", 2, new BigDecimal("50000"));
        } catch (IllegalStateException ex) { dup = true; }
        verificar("CP-23" + sufijo, "Rechazar ID duplicado [" + sufijo + "]", dup);

        g.registrar(2, "Shiryu", Rango.BRONCE, "Dragón", 8, "Rozan", 3, new BigDecimal("150000"));
        g.registrar(3, "Hyoga", Rango.BRONCE, "Cisne", 7, "Siberia", 2, new BigDecimal("100000"));

        Caballero mod = g.modificar(2, "Shiryu Maestro", Rango.BRONCE, "Dragón", 9, "Rozan", 3, new BigDecimal("250000"));
        verificar("CP-24" + sufijo, "Modificar por ID [" + sufijo + "]", mod.getNombre().equals("Shiryu Maestro"));

        List<Caballero> lista = g.listar();
        verificar("CP-25" + sufijo, "Conservar orden de inserción tras modificar [" + sufijo + "]",
                  lista.get(0).getId() == 1 && lista.get(1).getId() == 2 && lista.get(2).getId() == 3);

        boolean inexistente = false;
        try {
            g.modificar(99, "X", Rango.ORO, "Leo", 5, "M", 1, BigDecimal.TEN);
        } catch (NoSuchElementException ex) { inexistente = true; }
        verificar("CP-26" + sufijo, "Rechazar modificación de ID inexistente [" + sufijo + "]", inexistente);
        verificar("CP-27" + sufijo, "Buscar ID inexistente devuelve vacío [" + sufijo + "]", g.buscar(99).isEmpty());
    }

    public static void main(String[] args) {
        CalculadoraFinanciera calc = new CalculadoraFinanciera();

        CalculadoraFinanciera.Resultado r1 = calc.calcular(new BigDecimal("50000"));
        verificar("CP-01", "Tramo 0% (50,000)", r1.impuesto().compareTo(BigDecimal.ZERO) == 0 && r1.neta().compareTo(new BigDecimal("45000.00")) == 0);

        CalculadoraFinanciera.Resultado r2 = calc.calcular(new BigDecimal("100000"));
        verificar("CP-02", "Límite 100,000", r2.impuesto().compareTo(BigDecimal.ZERO) == 0);

        CalculadoraFinanciera.Resultado r3 = calc.calcular(new BigDecimal("150000"));
        verificar("CP-03", "Tramo 12% (150,000)", r3.impuesto().compareTo(new BigDecimal("6000.00")) == 0 && r3.neta().compareTo(new BigDecimal("129000.00")) == 0);

        CalculadoraFinanciera.Resultado r5 = calc.calcular(new BigDecimal("300000"));
        verificar("CP-05", "Tramo 25% progresivo (300,000)", r5.impuesto().compareTo(new BigDecimal("37000.00")) == 0);

        CalculadoraFinanciera.Resultado r7 = calc.calcular(new BigDecimal("500000"));
        verificar("CP-07", "Tramo 35% progresivo (500,000)", r7.impuesto().compareTo(new BigDecimal("97000.00")) == 0);

        calc.setMetodo(CalculadoraFinanciera.MetodoImpuesto.DIRECTO);
        verificar("CP-11", "Método directo (300,000)", calc.impuesto(new BigDecimal("300000")).compareTo(new BigDecimal("25000.00")) == 0);
        verificar("CP-12", "Método directo (500,000)", calc.impuesto(new BigDecimal("500000")).compareTo(new BigDecimal("35000.00")) == 0);

        probarRepositorio("A", new RepositorioLista());
        probarRepositorio("B", new RepositorioMapa());

        GestorCaballeros gA = new GestorCaballeros(new RepositorioLista());
        GestorCaballeros gB = new GestorCaballeros(new RepositorioMapa());
        gA.cargarDatosDeEjemplo();
        gB.cargarDatosDeEjemplo();
        Informe infA = gA.generarInforme();
        Informe infB = gB.generarInforme();
        verificar("CP-31", "Equivalencia funcional entre ArrayList y LinkedHashMap",
                  infA.totalNeta().compareTo(infB.totalNeta()) == 0 && infA.filas().size() == 8);

        System.out.printf("%nRESUMEN: %d de %d pruebas aprobadas.%n", aprobados, totales);
    }
}
