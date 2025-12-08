package boletamaster.ui;

import boletamaster.app.Sistema;
import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import logica.BoletamasterSystem; 

public class WelcomeFrame extends JFrame {

    private final Sistema sistema;

    public WelcomeFrame(Sistema sistema) {
        this.sistema = sistema;
        setTitle("Bienvenido a BoleteraMaster");
        setSize(400, 200);
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                guardarDatosAlCerrar();
                System.exit(0);
            }
        });
        
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Seleccione una Opción:", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton btnLogin = new JButton("1. Iniciar Sesión");
        JButton btnRegister = new JButton("2. Registrarse");
        

        
        btnLogin.addActionListener(e -> {
            new LoginFrame(sistema).setVisible(true);
            this.dispose(); 
        });

        btnRegister.addActionListener(e -> {

            new RegisterFrame(sistema).setVisible(true); 
            this.dispose(); 
        });
        

        panel.add(titulo);
        panel.add(btnLogin);
        panel.add(btnRegister);

        add(panel);
    }
    
    private void guardarDatosAlCerrar() {
        BoletamasterSystem core = BoletamasterSystem.getInstance();
        
        System.out.println("Guardando datos al cerrar...");
        
        try {
            core.getGestorPersistencia().guardarUsuarios(core.getRepo().getUsuarios());
            core.getGestorPersistencia().guardarEventos(core.getRepo().getEventos());
            core.getGestorPersistencia().guardarVenues(core.getRepo().getVenues());
            core.getGestorPersistencia().guardarTickets(core.getRepo().getTickets());
            core.getGestorPersistencia().guardarTransacciones(core.getRepo().getTransacciones());
            core.getGestorPersistencia().guardarOfertas(core.getRepo().getOfertas());
            core.getGestorPersistencia().guardarLog(core.getRepo().getLog()); 
            
            System.out.println("Datos guardados exitosamente.");
        } catch (Exception e) {
            System.err.println("¡ERROR! Falló el guardado de datos al cerrar: " + e.getMessage());
        }
    }
}