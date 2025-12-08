package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.tiquetes.Ticket;
import javax.swing.*;
import java.awt.*;
import java.util.UUID;
import java.util.function.Supplier;

public class LocalidadTicketCreationFrame extends JDialog {
    private final Sistema sistema;
    private final Evento evento;
    private final Runnable callback;

    private JTextField txtNombreLocalidad;
    private JTextField txtPrecio;
    private JTextField txtCapacidad;
    private JCheckBox chkEsNumerada;

    public LocalidadTicketCreationFrame(JFrame owner, Sistema sistema, Evento evento, Runnable callback) {
        super(owner, "Crear Localidad y Tiquetes para: " + evento.getNombre(), true);
        this.sistema = sistema;
        this.evento = evento;
        this.callback = callback;
        
        setLayout(new BorderLayout(10, 10));
        setSize(450, 350);
        setLocationRelativeTo(owner);
        
        // Formulario
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        txtNombreLocalidad = new JTextField();
        txtPrecio = new JTextField();
        txtCapacidad = new JTextField();
        chkEsNumerada = new JCheckBox("Localidad con Asientos Numerados");

        formPanel.add(new JLabel("Nombre Localidad:"));
        formPanel.add(txtNombreLocalidad);
        formPanel.add(new JLabel("Precio Unitario:"));
        formPanel.add(txtPrecio);
        formPanel.add(new JLabel("Capacidad Total:"));
        formPanel.add(txtCapacidad);
        formPanel.add(new JLabel("Tipo de Tiquete:"));
        formPanel.add(chkEsNumerada);

        // Botón
        JButton btnCrear = new JButton("Crear Localidad y Generar Tiquetes");
        btnCrear.addActionListener(e -> crearLocalidadYTickets());
        
        add(formPanel, BorderLayout.CENTER);
        add(btnCrear, BorderLayout.SOUTH);
    }
    
    private void crearLocalidadYTickets() {
        try {
            String nombre = txtNombreLocalidad.getText().trim();
            double precio = Double.parseDouble(txtPrecio.getText().trim());
            int capacidad = Integer.parseInt(txtCapacidad.getText().trim());
            boolean esNumerada = chkEsNumerada.isSelected();

            if (nombre.isEmpty() || precio <= 0 || capacidad <= 0) {
                throw new IllegalArgumentException("Verifique nombre, precio y capacidad.");
            }

            
            Localidad nuevaLocalidad = new Localidad(UUID.randomUUID().toString(), nombre, precio, capacidad, esNumerada);
            evento.addLocalidad(nuevaLocalidad); 
            
            for (int i = 0; i < capacidad; i++) {
                Ticket t;
                if (esNumerada) {
                    String asiento = nombre.substring(0, 1).toUpperCase() + String.format("%03d", i + 1);
                    t = sistema.generarTicketNumerado(nuevaLocalidad, asiento);
                } else {
                    t = sistema.generarTicketSimple(nuevaLocalidad);
                }
                
            }

            JOptionPane.showMessageDialog(this, 
                "Localidad '" + nombre + "' creada y " + capacidad + " tiquetes generados.", 
                "Éxito", JOptionPane.INFORMATION_MESSAGE);
                
            callback.run(); 
            this.dispose();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Precio y Capacidad deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error al generar tiquetes: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}