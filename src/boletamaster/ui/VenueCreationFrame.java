package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.eventos.Venue;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.function.Supplier;

public class VenueCreationFrame extends JDialog {
    private final Sistema sistema;
    private final Runnable callback;
    
    private JTextField txtId;
    private JTextField txtNombre;
    private JTextField txtUbicacion;
    private JTextField txtCapacidad;
    
    public VenueCreationFrame(JFrame owner, Sistema sistema, Runnable callback) {
        super(owner, "Sugerir Nuevo Venue", true); // Modal
        this.sistema = sistema;
        this.callback = callback;
        
        setLayout(new BorderLayout(10, 10));
        setSize(400, 300);
        setLocationRelativeTo(owner);
        
        // Panel de formulario
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        txtId = new JTextField(UUID.randomUUID().toString().substring(0, 8));
        txtId.setEditable(false);
        txtNombre = new JTextField();
        txtUbicacion = new JTextField();
        txtCapacidad = new JTextField();
        
        formPanel.add(new JLabel("ID (Generado):"));
        formPanel.add(txtId);
        formPanel.add(new JLabel("Nombre Venue:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Ubicación:"));
        formPanel.add(txtUbicacion);
        formPanel.add(new JLabel("Capacidad Máx.:"));
        formPanel.add(txtCapacidad);
        
        // Botón
        JButton btnRegistrar = new JButton("Sugerir Venue (Requiere Aprobación)");
        btnRegistrar.addActionListener(e -> registrarVenue());
        
        add(formPanel, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }
    
    private void registrarVenue() {
        try {
            String id = txtId.getText();
            String nombre = txtNombre.getText().trim();
            String ubicacion = txtUbicacion.getText().trim();
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            
            if (nombre.isEmpty() || ubicacion.isEmpty() || capacidad <= 0) {
                throw new IllegalArgumentException("Campos incompletos o capacidad inválida.");
            }
            
            Venue nuevoVenue = new Venue(id, nombre, ubicacion, capacidad, true); 
            
            sistema.registrarVenue(nuevoVenue); 
            
            JOptionPane.showMessageDialog(this, "Venue sugerido exitosamente. Esperando aprobación del Administrador.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            callback.run(); 
            this.dispose();
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La capacidad debe ser un número entero.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al registrar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}