package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Comprador;
import boletamaster.usuarios.Organizador;
import boletamaster.usuarios.Administrador;
import boletamaster.usuarios.Usuario;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final Sistema sistema;
    private JComboBox<String> cmbTipoUsuario;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JTextField txtNombre; // Usado para Nombre (Comprador/Admin) o Nombre Empresa (Organizador)
    private JPanel dynamicPanel; // Panel para campos específicos
    
    // Identificadores de tipos
    private static final String TIPO_COMPRADOR = "Comprador";
    private static final String TIPO_ORGANIZADOR = "Organizador";
    private static final String TIPO_ADMIN = "Administrador";

    public RegisterFrame(Sistema sistema) {
        this.sistema = sistema;
        
        setTitle("Registro de Nuevo Usuario");
        setSize(450, 350);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        // --- Componentes Base ---
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // --- Panel de Formulario Central ---
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        
        cmbTipoUsuario = new JComboBox<>(new String[]{TIPO_COMPRADOR, TIPO_ORGANIZADOR, TIPO_ADMIN});
        txtLogin = new JTextField();
        txtPassword = new JPasswordField();
        txtNombre = new JTextField();

        formPanel.add(new JLabel("Tipo de Cuenta:"));
        formPanel.add(cmbTipoUsuario);
        formPanel.add(new JLabel("Login (Usuario):"));
        formPanel.add(txtLogin);
        formPanel.add(new JLabel("Contraseña:"));
        formPanel.add(txtPassword);
        formPanel.add(new JLabel("Nombre (Persona/Empresa):"));
        formPanel.add(txtNombre);
        
        // --- Botones ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnRegistrar = new JButton("Registrar");
        JButton btnVolver = new JButton("Volver");

        buttonPanel.add(btnVolver);
        buttonPanel.add(btnRegistrar);

        // --- Action Listeners ---
        btnRegistrar.addActionListener(e -> intentarRegistro());
        btnVolver.addActionListener(e -> volverAWelcome());

        // --- Ensamblaje ---
        panel.add(formPanel, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    private void intentarRegistro() {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        String nombreOEmpresa = txtNombre.getText().trim();
        String tipo = (String) cmbTipoUsuario.getSelectedItem(); // Esto es el tipo (Comprador, Organizador, Administrador)

        if (login.isEmpty() || password.isEmpty() || nombreOEmpresa.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos los campos son obligatorios.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // 1. Verificar si el usuario ya existe
            Usuario existente = sistema.buscarUsuarioPorLogin(login);
            if (existente != null) {
                JOptionPane.showMessageDialog(this, 
                    "El login '" + login + "' ya está en uso.", 
                    "Error de Registro", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            // 2. LLAMADA CORREGIDA: Usamos la firma que existe en Sistema.java
            sistema.registrarUsuario(login, password, nombreOEmpresa, tipo);

            // 3. Mostrar éxito y navegar
            JOptionPane.showMessageDialog(this, 
                tipo + " '" + login + "' registrado exitosamente. Ya puedes iniciar sesión.", 
                "Éxito", 
                JOptionPane.INFORMATION_MESSAGE);
            
            new LoginFrame(sistema).setVisible(true);
            this.dispose(); 

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, 
                "Error de validación: " + ex.getMessage(), 
                "Error", 
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