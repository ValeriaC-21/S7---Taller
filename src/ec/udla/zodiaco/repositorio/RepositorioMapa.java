package ec.udla.zodiaco.repositorio;

import ec.udla.zodiaco.modelo.Caballero;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RepositorioMapa implements RepositorioCaballeros {
    private final LinkedHashMap<Integer, Caballero> datos = new LinkedHashMap<>();

    @Override
    public void registrar(Caballero caballero) {
        if (datos.putIfAbsent(caballero.getId(), caballero) != null) {
            throw new IllegalStateException("Ya existe un caballero con el ID " + caballero.getId() + ".");
        }
    }

    @Override
    public void actualizar(Caballero caballero) {
        if (!datos.containsKey(caballero.getId())) {
            throw new NoSuchElementException("No existe un caballero con el ID " + caballero.getId() + ".");
        }
        datos.put(caballero.getId(), caballero);
    }

    @Override
    public Optional<Caballero> buscarPorId(int id) {
        return Optional.ofNullable(datos.get(id));
    }

    @Override
    public boolean existe(int id) {
        return datos.containsKey(id);
    }

    @Override
    public List<Caballero> listar() {
        return Collections.unmodifiableList(new ArrayList<>(datos.values()));
    }

    @Override
    public int cantidad() {
        return datos.size();
    }

    @Override
    public String nombreEstructura() {
        return "LinkedHashMap<Integer, Caballero> (tabla hash con orden de inserción)";
    }
}
