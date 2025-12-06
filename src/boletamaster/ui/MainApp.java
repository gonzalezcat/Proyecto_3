package boletamaster.ui;

import boletamaster.app.Sistema;
import logica.BoletamasterSystem;

import javax.swing.SwingUtilities;

public class MainApp {
    
    public static void main(String[] args) {
        BoletamasterSystem core = BoletamasterSystem.getInstance();
        Sistema sistema = new Sistema(core);

        SwingUtilities.invokeLater(() -> {
            // --- CAMBIO CLAVE: Muestra WelcomeFrame ---
            new WelcomeFrame(sistema).setVisible(true);
        });
    }
}