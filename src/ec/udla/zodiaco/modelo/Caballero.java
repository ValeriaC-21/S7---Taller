package ec.udla.zodiaco.modelo;

import java.math.BigDecimal;
import java.util.Objects;

public final class Caballero implements Comparable<Caballero> {
    private final int id;
    private final String nombre;
    private final Rango rango;
    private final String constelacion;
    private final int nivelPoder;
    private final String mision;
    private final int nivelDificultad;
    private final BigDecimal recompensa;

    public Caballero(int id, String nombre, Rango rango, String constelacion,
                     int nivelPoder, String mision, int nivelDificultad, BigDecimal recompensa) {
        if (id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un entero positivo.");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new IllegalArgumentException("El nombre del caballero es obligatorio.");
        }
        if (rango == null) {
            throw new IllegalArgumentException("El rango es obligatorio.");
        }
        if (constelacion == null || constelacion.isBlank()) {
            throw new IllegalArgumentException("La constelación es obligatoria.");
        }
        if (nivelPoder < 1 || nivelPoder > 10) {
            throw new IllegalArgumentException("El nivel de poder debe estar entre 1 y 10.");
        }
        if (mision == null || mision.isBlank()) {
            throw new IllegalArgumentException("La misión asignada es obligatoria.");
        }
        if (nivelDificultad < 1 || nivelDificultad > 5) {
            throw new IllegalArgumentException("El nivel de dificultad debe estar entre 1 y 5.");
        }
        if (recompensa == null || recompensa.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("La recompensa no puede ser negativa.");
        }

        this.id = id;
        this.nombre = nombre.trim();
        this.rango = rango;
        this.constelacion = constelacion.trim();
        this.nivelPoder = nivelPoder;
        this.mision = mision.trim();
        this.nivelDificultad = nivelDificultad;
        this.recompensa = recompensa;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public Rango getRango() { return rango; }
    public String getConstelacion() { return constelacion; }
    public int getNivelPoder() { return nivelPoder; }
    public String getMision() { return mision; }
    public int getNivelDificultad() { return nivelDificultad; }
    public BigDecimal getRecompensa() { return recompensa; }

    @Override
    public int compareTo(Caballero otro) {
        return Integer.compare(this.id, otro.id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Caballero that)) return false;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
