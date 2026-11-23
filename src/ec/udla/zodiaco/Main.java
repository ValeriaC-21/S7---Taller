package ec.udla.zodiaco;

import ec.udla.zodiaco.repositorio.RepositorioMapa;
import ec.udla.zodiaco.servicio.GestorCaballeros;
import ec.udla.zodiaco.vista.VentanaSantuario;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            GestorCaballeros gestor = new GestorCaballeros(new RepositorioMapa());
            VentanaSantuario ventana = new VentanaSantuario(gestor);
            ventana.setVisible(true);
        });
    }
}
