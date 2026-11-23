package ec.udla.zodiaco.pruebas;

import ec.udla.zodiaco.modelo.Caballero;
import ec.udla.zodiaco.modelo.Rango;
import ec.udla.zodiaco.repositorio.RepositorioCaballeros;
import ec.udla.zodiaco.repositorio.RepositorioLista;
import ec.udla.zodiaco.repositorio.RepositorioMapa;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BenchmarkEstructuras {
    private static List<Caballero> generar(int n) {
        List<Caballero> lista = new ArrayList<>(n);
        Rango[] rangos = Rango.values();
        for (int i = 1; i <= n; i++) {
            lista.add(new Caballero(
                i, "Caballero_" + i, rangos[i % 3], "Constelacion_" + (i % 88),
                (i % 10) + 1, "Mision_" + i, (i % 5) + 1, BigDecimal.valueOf(50000L + (i % 500) * 1000L)
            ));
        }
        return lista;
    }

    private static double medirRegistro(RepositorioCaballeros repo, List<Caballero> datos) {
        long t0 = System.nanoTime();
        for (Caballero c : datos) repo.registrar(c);
        return (System.nanoTime() - t0) / 1_000_000.0;
    }

    private static double medirBusquedaModificacion(RepositorioCaballeros repo, int[] ids) {
        long t0 = System.nanoTime();
        for (int id : ids) {
            Caballero orig = repo.buscarPorId(id).orElseThrow();
            Caballero mod = new Caballero(orig.getId(), orig.getNombre(), orig.getRango(), orig.getConstelacion(),
                    orig.getNivelPoder(), orig.getMision(), orig.getNivelDificultad(), orig.getRecompensa().add(BigDecimal.TEN));
            repo.actualizar(mod);
        }
        return (System.nanoTime() - t0) / 1_000_000.0;
    }

    public static void main(String[] args) {
        int[] tamanos = {500, 1000, 5000, 10000, 20000, 50000};
        Random rnd = new Random(42);
        System.out.println("n	Registrar_A(ms)	Registrar_B(ms)	BusqMod_A(ms)	BusqMod_B(ms)");
        for (int n : tamanos) {
            List<Caballero> datos = generar(n);
            int[] consultas = new int[5000];
            for (int i = 0; i < consultas.length; i++) consultas[i] = rnd.nextInt(n) + 1;

            RepositorioLista repoA = new RepositorioLista();
            RepositorioMapa repoB = new RepositorioMapa();
            double regA = medirRegistro(repoA, datos);
            double regB = medirRegistro(repoB, datos);
            double bmA = medirBusquedaModificacion(repoA, consultas);
            double bmB = medirBusquedaModificacion(repoB, consultas);
            System.out.printf("%d	%.3f		%.3f		%.3f		%.3f%n", n, regA, regB, bmA, bmB);
        }
    }
}
