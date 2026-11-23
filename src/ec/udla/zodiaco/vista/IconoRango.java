package ec.udla.zodiaco.vista;

import ec.udla.zodiaco.modelo.Rango;
import java.awt.*;
import javax.swing.Icon;

public class IconoRango implements Icon {
    private Rango rango;
    private final int size;

    public IconoRango(Rango rango, int size) {
        this.rango = rango;
        this.size = size;
    }

    public void setRango(Rango rango) {
        this.rango = rango;
    }

    @Override
    public int getIconWidth() { return size; }

    @Override
    public int getIconHeight() { return size; }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Color principal, borde;
        switch (rango) {
            case ORO -> { principal = new Color(230, 180, 34); borde = new Color(150, 105, 10); }
            case PLATA -> { principal = new Color(192, 198, 206); borde = new Color(100, 108, 118); }
            default -> { principal = new Color(176, 112, 60); borde = new Color(102, 58, 24); }
        }

        int w = size - 8;
        int h = size - 8;
        int cx = x + size / 2;
        int top = y + 4;

        Polygon escudo = new Polygon();
        escudo.addPoint(cx - w / 2, top);
        escudo.addPoint(cx + w / 2, top);
        escudo.addPoint(cx + w / 2, top + h / 2);
        escudo.addPoint(cx, top + h);
        escudo.addPoint(cx - w / 2, top + h / 2);

        g2.setColor(principal);
        g2.fillPolygon(escudo);
        g2.setStroke(new BasicStroke(3f));
        g2.setColor(borde);
        g2.drawPolygon(escudo);

        // Estrella central
        int rExt = w / 4;
        int rInt = rExt / 2;
        int cy = top + h / 2 - 2;
        Polygon estrella = new Polygon();
        for (int i = 0; i < 10; i++) {
            double ang = Math.toRadians(-90 + i * 36);
            int r = (i % 2 == 0) ? rExt : rInt;
            estrella.addPoint(cx + (int) Math.round(r * Math.cos(ang)),
                              cy + (int) Math.round(r * Math.sin(ang)));
        }
        g2.setColor(Color.WHITE);
        g2.fillPolygon(estrella);
        g2.setStroke(new BasicStroke(1.5f));
        g2.setColor(borde);
        g2.drawPolygon(estrella);
        g2.dispose();
    }
}
