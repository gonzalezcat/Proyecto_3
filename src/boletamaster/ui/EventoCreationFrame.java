package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Venue;
import boletamaster.usuarios.Organizador;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.function.Supplier;

public class EventoCreationFrame extends JDialog {
    private final Sistema sistema;
    private final Organizador organizador;
    private final Runnable callback;
    
    private JTextField txtNombre;
    private JTextField txtFechaHora; 
    private JComboBox<Venue> cmbVenue;
    
    public EventoCreationFrame(JFrame owner, Sistema sistema, Organizador organizador, Runnable callback) {
        super(owner, "Crear Nuevo Evento", true);
        this.sistema = sistema;
        this.organizador = organizador;
        this.callback = callback;
        
        setLayout(new BorderLayout(10, 10));
        setSize(450, 300);
        setLocationRelativeTo(owner);
        
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        txtNombre = new JTextField();
        txtFechaHora = new JTextField("yyyy-MM-dd HH:mm");
        cmbVenue = new JComboBox<>();
        cargarVenues();
        
        formPanel.add(new JLabel("Nombre Evento:"));
        formPanel.add(txtNombre);
        formPanel.add(new JLabel("Fecha/Hora (YYYY-MM-DD HH:MM):"));
        formPanel.add(txtFechaHora);
        formPanel.add(new JLabel("Seleccionar Venue:"));
        formPanel.add(cmbVenue);
        
        JButton btnRegistrar = new JButton("Crear Evento");
        btnRegistrar.addActionListener(e -> registrarEvento());
        
        add(formPanel, BorderLayout.CENTER);
        add(btnRegistrar, BorderLayout.SOUTH);
    }
    
    private void cargarVenues() {
        
        sistema.getRepo().getVenues().forEach(cmbVenue::addItem);
    }
    
    private void registrarEvento() {
        try {
            String nombre = txtNombre.getText().trim();
            String fechaStr = txtFechaHora.getText().trim();
            Venue venueSeleccionado = (Venue) cmbVenue.getSelectedItem();
            
            if (nombre.isEmpty() || fechaStr.isEmpty() || venueSeleccionado == null) {
                throw new IllegalArgumentException("Todos los campos son obligatorios.");
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime fecha = LocalDateTime.parse(fechaStr, formatter);
            
            sistema.registrarEvento(organizador, nombre, fecha, venueSeleccionado);
            
            JOptionPane.showMessageDialog(this, "Evento creado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            callback.run(); 
            this.dispose();
            
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(this, "Formato de fecha/hora incorrecto. Use YYYY-MM-DD HH:MM.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al crear evento: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}