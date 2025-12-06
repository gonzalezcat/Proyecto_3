package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Organizador;
import boletamaster.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final Sistema sistema;
    
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JTextField txtNombreOrganizacion;
    private JButton btnRegistrar;
    private JButton btnVolver;

    public RegisterFrame(Sistema sistema) {
        this.sistema = sistema;
        
        setTitle("Registro de Nuevo Organizador");
        setSize(450, 300);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel lblTitulo = new JLabel("Crear Cuenta de Organizador", SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        panel.add(lblTitulo, gbc);
        
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Login (Usuario):"), gbc);
        
        txtLogin = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtLogin, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Contraseña:"), gbc);
        
        txtPassword = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        panel.add(new JLabel("Nombre Organización:"), gbc);
        
        txtNombreOrganizacion = new JTextField(20);
        gbc.gridx = 1;
        panel.add(txtNombreOrganizacion, gbc);

        btnRegistrar = new JButton("Registrar");
        btnVolver = new JButton("Volver");
        
        gbc.gridy = 4;
        gbc.gridx = 0;
        panel.add(btnVolver, gbc);
        
        gbc.gridx = 1;
        panel.add(btnRegistrar, gbc);
        
        btnRegistrar.addActionListener(e -> intentarRegistro());
        btnVolver.addActionListener(e -> volverAWelcome());

        add(panel, BorderLayout.CENTER);
    }


    private void intentarRegistro() {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String nombreOrg = txtNombreOrganizacion.getText().trim();

        if (login.isEmpty() || password.isEmpty() || nombreOrg.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Todos los campos son obligatorios.", 
                "Error de Validación", 
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            Usuario existente = sistema.buscarUsuarioPorLogin(login);
            if (existente != null) {
                JOptionPane.showMessageDialog(this, 
                    "El login '" + login + "' ya está en uso.", 
                    "Error de Registro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            Organizador nuevoOrganizador = new Organizador(login, password, nombreOrg);

            sistema.registrarUsuario(login, password, nombreOrg, "Organizador");

            JOptionPane.showMessageDialog(this, 
                "¡Registro exitoso! Ya puedes iniciar sesión.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            new LoginFrame(sistema).setVisible(true);
            this.dispose(); 
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error en el sistema: " + ex.getMessage(), 
                "Error Interno", 
                JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Ocurrió un error inesperado al registrar.", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }


    private void volverAWelcome() {
        new WelcomeFrame(sistema).setVisible(true);
        this.dispose();
    }
}