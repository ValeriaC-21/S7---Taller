package ec.udla.zodiaco.repositorio;

import ec.udla.zodiaco.modelo.Caballero;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

public class RepositorioLista implements RepositorioCaballeros {
    private final ArrayList<Caballero> datos = new ArrayList<>();

    private int indiceDe(int id) {
        for (int i = 0; i < datos.size(); i++) {
            if (datos.get(i).getId() == id) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void registrar(Caballero caballero) {
        if (indiceDe(caballero.getId()) >= 0) {
            throw new IllegalStateException("Ya existe un caballero con el ID " + caballero.getId() + ".");
        }
        datos.add(caballero);
    }

    @Override
    public void actualizar(Caballero caballero) {
        int idx = indiceDe(caballero.getId());
        if (idx < 0) {
            throw new NoSuchElementException("No existe un caballero con el ID " + caballero.getId() + ".");
        }
        datos.set(idx, caballero);
    }

    @Override
    public Optional<Caballero> buscarPorId(int id) {
        int idx = indiceDe(id);
        return idx >= 0 ? Optional.of(datos.get(idx)) : Optional.empty();
    }

    @Override
    public boolean existe(int id) {
        return indiceDe(id) >= 0;
    }

    @Override
    public List<Caballero> listar() {
        return Collections.unmodifiableList(new ArrayList<>(datos));
    }

    @Override
    public int cantidad() {
        return datos.size();
    }

    @Override
    public String nombreEstructura() {
        return "ArrayList<Caballero> (lista dinámica secuencial)";
    }
}
