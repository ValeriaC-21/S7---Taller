package ec.udla.zodiaco.modelo;

public enum Rango {
    BRONCE("Bronce"),
    PLATA("Plata"),
    ORO("Oro");

    private final String etiqueta;

    Rango(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    @Override
    public String toString() {
        return etiqueta;
    }
}
