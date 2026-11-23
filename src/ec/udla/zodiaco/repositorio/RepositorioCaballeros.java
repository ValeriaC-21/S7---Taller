package ec.udla.zodiaco.repositorio;

import ec.udla.zodiaco.modelo.Caballero;
import java.util.List;
import java.util.Optional;

public interface RepositorioCaballeros {
    void registrar(Caballero caballero);
    void actualizar(Caballero caballero);
    Optional<Caballero> buscarPorId(int id);
    boolean existe(int id);
    List<Caballero> listar();
    int cantidad();
    String nombreEstructura();
}
