
package boletamaster.ui;

import boletamaster.app.Sistema;
import javax.swing.*;
import java.awt.*;

public class WelcomeFrame extends JFrame {

    private final Sistema sistema;

    public WelcomeFrame(Sistema sistema) {
        this.sistema = sistema;
        setTitle("Bienvenido a BoleteraMaster");
        setSize(400, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
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
}