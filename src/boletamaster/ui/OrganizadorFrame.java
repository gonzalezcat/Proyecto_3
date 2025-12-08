package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Organizador;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Venue;
import boletamaster.tiquetes.Ticket;
import boletamaster.tiquetes.TicketEstado;
import boletamaster.tiquetes.TicketMultiple;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrganizadorFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    private final Sistema sistema;
    private final Organizador organizador;
    private JTabbedPane tabbedPane;
    private DefaultTableModel eventosModel;
    private DefaultTableModel venuesModel;


    public OrganizadorFrame(Sistema sistema, Organizador organizador) {
        this.sistema = sistema;
        this.organizador = organizador;

        setTitle("BoletaMaster - Organizador: " + organizador.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        tabbedPane = new JTabbedPane();

        tabbedPane.addTab("1. Gestión de Eventos", createGestionEventosPanel());
        tabbedPane.addTab("2. Gestión de Venues", createGestionVenuesPanel());
        tabbedPane.addTab("3. Reportes de Ventas", createReportesPanel());

        add(tabbedPane, BorderLayout.CENTER);
        setupMenuBar();
    }

    // --- PESTAÑA 1: GESTIÓN DE EVENTOS 
    private JPanel createGestionEventosPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        String[] columnas = {"ID", "Nombre", "Fecha", "Venue", "Tickets Disp.", "Publicado"}; 
        eventosModel = new DefaultTableModel(columnas, 0) {
             @Override
             public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable eventosTable = new JTable(eventosModel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrearEvento = new JButton("Crear Nuevo Evento");
        JButton btnCrearLocalidad = new JButton("Crear Localidad/Tickets");
        JButton btnPublicarEvento = new JButton("Publicar/Despublicar en Marketplace");

        btnCrearEvento.addActionListener(e -> new EventoCreationFrame(this, sistema, organizador, this::actualizarTablas).setVisible(true));
        btnCrearLocalidad.addActionListener(e -> gestionarLocalidadesTiquetes(eventosTable));
        btnPublicarEvento.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Publicación Pendiente."));
        
        buttonPanel.add(btnCrearEvento);
        buttonPanel.add(btnCrearLocalidad);
        buttonPanel.add(btnPublicarEvento);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(eventosTable), BorderLayout.CENTER);
        
        actualizarTablaEventos();
        return panel;
    }

    private void gestionarLocalidadesTiquetes(JTable table) {
        int filaSeleccionada = table.getSelectedRow();
        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un evento de la lista primero.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String idEvento = (String) table.getValueAt(filaSeleccionada, 0);
        Evento evento = sistema.buscarEventoPorIdYOrganizador(idEvento, organizador);

        if (evento != null) {
            new LocalidadTicketCreationFrame(this, sistema, evento, this::actualizarTablas).setVisible(true);
        } else {
            JOptionPane.showMessageDialog(this, "Error: No se encontró el evento seleccionado.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    // --- PESTAÑA 2
    private JPanel createGestionVenuesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        String[] columnas = {"ID", "Nombre", "Ubicación", "Capacidad Máxima", "¿Aprobado?"};
        venuesModel = new DefaultTableModel(columnas, 0) {
             @Override
             public boolean isCellEditable(int row, int column) { return false; }
        };
        JTable venuesTable = new JTable(venuesModel);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnCrearVenue = new JButton("Sugerir Nuevo Venue");

        btnCrearVenue.addActionListener(e -> new VenueCreationFrame(this, sistema, this::actualizarTablas).setVisible(true));
        
        buttonPanel.add(btnCrearVenue);
        
        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(venuesTable), BorderLayout.CENTER);
        
        actualizarTablaVenues();
        return panel;
    }

    // --- PESTAÑA 3
    private JPanel createReportesPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        JComboBox<Evento> cmbEventos = new JComboBox<>();
        cmbEventos.addItem(null); // Opción por defecto
        sistema.eventosActivosPorOrganizador(organizador).forEach(cmbEventos::addItem);

        JTextArea txtReporte = new JTextArea(15, 50);
        txtReporte.setEditable(false);
        
        JButton btnGenerarReporte = new JButton("Generar Reporte de Ventas");
        
        btnGenerarReporte.addActionListener(e -> {
            Evento eventoSeleccionado = (Evento) cmbEventos.getSelectedItem();
            if (eventoSeleccionado != null) {
                mostrarReporteVentas(eventoSeleccionado, txtReporte);
            } else {
                JOptionPane.showMessageDialog(this, "Seleccione un evento.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(new JLabel("Seleccione Evento:"));
        controlPanel.add(cmbEventos);
        controlPanel.add(btnGenerarReporte);
        
        panel.add(controlPanel, BorderLayout.NORTH);
        panel.add(new JScrollPane(txtReporte), BorderLayout.CENTER);

        return panel;
    }

    // metodos

    public void actualizarTablas() {
        actualizarTablaEventos();
        actualizarTablaVenues();
    }

    private void actualizarTablaEventos() {
        eventosModel.setRowCount(0);
        List<Evento> eventos = sistema.eventosActivosPorOrganizador(organizador);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        
        for (Evento e : eventos) {
            int ticketsDisponibles = (int) sistema.getRepo().getTickets().stream()
                                    .filter(t -> t.getEvento() != null && t.getEvento().getId().equals(e.getId()))
                                    .count(); 
            
            eventosModel.addRow(new Object[]{
                e.getId(), 
                e.getNombre(), 
                e.getFechaHora().format(formatter), 
                e.getVenue().getNombre(),
                ticketsDisponibles,
                e.isPublicado() ? "Sí" : "No" 
            });
        }
    }
    
    private void actualizarTablaVenues() {
        venuesModel.setRowCount(0);
        List<Venue> allVenues = sistema.getRepo().getVenues(); 
        
        for (Venue v : allVenues) { 
            venuesModel.addRow(new Object[]{
                v.getId(), 
                v.getNombre(), 
                v.getUbicacion(), 
                v.getCapacidadMaxima(), 
                v.isAprobado() ? "Sí" : "No" 
            });
        }
    }

    private void mostrarReporteVentas(Evento evento, JTextArea txtReporte) {
        txtReporte.setText("");
        txtReporte.append("--- REPORTE DE VENTAS PARA: " + evento.getNombre() + " ---\n");
        
        List<Ticket> ticketsDelEvento = sistema.getRepo().getTickets().stream()
                                        .filter(t -> t.getEvento() != null && t.getEvento().getId().equals(evento.getId()))
                                        .toList();
        
        int vendidos = 0;
        int disponibles = 0;
        Map<String, Integer> ventasPorTipo = new HashMap<>();
        ventasPorTipo.put("SIMPLE", 0);
        ventasPorTipo.put("NUMERADO", 0);
        
        for (Ticket t : ticketsDelEvento) {
            if (t.getEstado() == TicketEstado.VENDIDO || t.getEstado() == TicketEstado.TRANSFERIDO) {
                vendidos++;
                if (t instanceof boletamaster.tiquetes.TicketNumerado) {
                    ventasPorTipo.put("NUMERADO", ventasPorTipo.get("NUMERADO") + 1);
                } else {
                    if (!(t instanceof TicketMultiple) && !(t instanceof boletamaster.tiquetes.TicketDeluxe)) {
                        ventasPorTipo.put("SIMPLE", ventasPorTipo.get("SIMPLE") + 1);
                    }
                }
            } else if (t.getEstado() == TicketEstado.DISPONIBLE) {
                disponibles++;
            }
        }
        
        txtReporte.append("Total de Tiquetes Emitidos: " + ticketsDelEvento.size() + "\n");
        txtReporte.append("Tiquetes Vendidos: " + vendidos + "\n");
        txtReporte.append("Tiquetes Disponibles: " + disponibles + "\n\n");
        txtReporte.append("--- VENTAS POR TIPO ---\n");
        txtReporte.append("Tiquetes Simples Vendidos: " + ventasPorTipo.get("SIMPLE") + "\n");
        txtReporte.append("Tiquetes Numerados Vendidos: " + ventasPorTipo.get("NUMERADO") + "\n");
        txtReporte.append("\n");
        
    }
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesión");
        
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    private void logout() {
        this.dispose();
        new LoginFrame(sistema).setVisible(true);
    }
}
