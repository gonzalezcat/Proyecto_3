package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Administrador;
import boletamaster.usuarios.Comprador;
import boletamaster.usuarios.Organizador;
import boletamaster.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {
	
	private static final long serialVersionUID = 1L;

    private final Sistema sistema;
    private final JTextField userField;
    private final JPasswordField passField;

    public LoginFrame(Sistema sistema) {
        this.sistema = sistema;
        setTitle("BoletaMaster - Iniciar Sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);
        
        userField = new JTextField(15);
        passField = new JPasswordField(15);
        JButton loginButton = new JButton("Iniciar Sesión");
        JLabel titleLabel = new JLabel("ACCESO AL SISTEMA", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        
        JButton btnVolver = new JButton("Volver");
        btnVolver.addActionListener(e -> {
            new WelcomeFrame(sistema).setVisible(true);
            this.dispose();
        });
        
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(titleLabel, gbc);

        gbc.gridy = 1;
        gbc.gridwidth = 1;
        panel.add(new JLabel("Usuario:"), gbc);
        
        gbc.gridx = 1;
        panel.add(userField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        
        gbc.gridx = 1;
        panel.add(passField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        add(panel);

        loginButton.addActionListener(e -> attemptLogin());
        
        userField.setText("juanito");
        passField.setText("123");
    }

    private void attemptLogin() {
        String login = userField.getText();
        String password = new String(passField.getPassword());

        Usuario user = sistema.buscarUsuarioPorLogin(login); 

        if (user != null && user.checkPassword(password)) {
            JOptionPane.showMessageDialog(this, "Bienvenido " + user.getNombre(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
            
            this.dispose(); 
            
            if (user instanceof Comprador c) {
                new CompradorFrame(sistema, c).setVisible(true);
            } else if (user instanceof Organizador o) {
                new OrganizadorFrame(sistema, o).setVisible(true); 
            } else if (user instanceof Administrador a) {
                new AdministradorFrame(sistema, a).setVisible(true); 
            }
        } else {
            JOptionPane.showMessageDialog(this, "Credenciales inválidas.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}