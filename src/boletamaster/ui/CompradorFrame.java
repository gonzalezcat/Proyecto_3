package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Comprador;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.List;

public class CompradorFrame extends JFrame {

    private final Sistema sistema;
    private final Comprador comprador;

    public CompradorFrame(Sistema sistema, Comprador comprador) {
        this.sistema = sistema;
        this.comprador = comprador;

        setTitle("BoleteraMaster - Comprador: " + comprador.getLogin());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.addTab("Perfil y Saldo", crearPanelPerfil());
        tabbedPane.addTab("Mis Tiquetes", crearPanelTiquetes());
        tabbedPane.addTab("Marketplace", crearPanelMarketplace());

        add(tabbedPane);
    }

    private JPanel crearPanelPerfil() {
        JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        panel.add(new JLabel("ID Usuario:"));
        panel.add(new JLabel(comprador.getId()));

        panel.add(new JLabel("Nombre Completo:"));
        panel.add(new JLabel(comprador.getNombre()));
        
        panel.add(new JLabel("Login:"));
        panel.add(new JLabel(comprador.getLogin()));

        panel.add(new JLabel("Saldo Disponible:"));
        JLabel lblSaldo = new JLabel(String.format("$%.2f", comprador.getSaldo()));
        lblSaldo.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(lblSaldo);

        return panel;
    }

    // --- Pestaña 2: Mis Tiquetes ---
    private JPanel crearPanelTiquetes() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        JTabbedPane ticketTabs = new JTabbedPane();
        
        List<Ticket> tickets = sistema.obtenerTicketsPorPropietario(comprador);
        
        DefaultTableModel modeloVigentes = crearModeloTickets();
        DefaultTableModel modeloVencidos = crearModeloTickets();
        
        LocalDateTime ahora = LocalDateTime.now();

        for (Ticket t : tickets) {
            Object[] fila = new Object[]{
                t.getId(),
                t.getEvento() != null ? t.getEvento().getNombre() : "N/A",
                t.getLocalidad() != null ? t.getLocalidad().getNombre() : "N/A",
                t.getPrecioFinal(),
                t.getEvento() != null ? t.getEvento().getFechaHora().toLocalDate() : "N/A",
                t
            };

            if (t.getEvento() != null && t.getEvento().getFechaHora().isAfter(ahora)) {
                modeloVigentes.addRow(fila);
            } else {
                modeloVencidos.addRow(fila);
            }
        }
        
        ticketTabs.addTab("Vigentes", crearPanelTabla(modeloVigentes, true));
        ticketTabs.addTab("Vencidos", crearPanelTabla(modeloVencidos, false));
        
        panel.add(ticketTabs, BorderLayout.CENTER);
        return panel;
    }

    private DefaultTableModel crearModeloTickets() {
        String[] columnas = {"ID Ticket", "Evento", "Localidad", "Precio Pagado", "Fecha Evento", "Objeto"};
        return new DefaultTableModel(columnas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };
    }

    private JPanel crearPanelTabla(DefaultTableModel modelo, boolean incluirBotonImprimir) {
        JPanel panel = new JPanel(new BorderLayout());
        JTable tabla = new JTable(modelo);
        
        tabla.getColumnModel().getColumn(5).setMinWidth(0);
        tabla.getColumnModel().getColumn(5).setMaxWidth(0);
        tabla.getColumnModel().getColumn(5).setWidth(0);

        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        if (incluirBotonImprimir) {
            JButton btnImprimir = new JButton("Imprimir Tiquete(s) Seleccionado(s)");
            btnImprimir.addActionListener(e -> imprimirTiquetesSeleccionados(tabla));
            JPanel botonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            botonPanel.add(btnImprimir);
            panel.add(botonPanel, BorderLayout.SOUTH);
        }

        return panel;
    }

    private void imprimirTiquetesSeleccionados(JTable tabla) {
        int[] filasSeleccionadas = tabla.getSelectedRows();
        if (filasSeleccionadas.length == 0) {
            JOptionPane.showMessageDialog(this, "Seleccione al menos un tiquete para imprimir.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        StringBuilder sb = new StringBuilder("Imprimiendo Tiquetes:\n");
        DefaultTableModel modelo = (DefaultTableModel) tabla.getModel();
        
        for (int row : filasSeleccionadas) {
            Ticket t = (Ticket) modelo.getValueAt(row, 5); 
            sb.append("- ").append(t.getId()).append(" | Evento: ").append(t.getEvento().getNombre()).append("\n");
        }
        
        JOptionPane.showMessageDialog(this, sb.toString(), "Simulación de Impresión", JOptionPane.INFORMATION_MESSAGE);
    }
    
    // --- Pestaña 3: Marketplace ---
    private JPanel crearPanelMarketplace() {
        JPanel panel = new JPanel(new GridBagLayout());
        
        JLabel lblTitulo = new JLabel("Explorar Eventos Disponibles");
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 18));
        
        JButton btnIrMarketplace = new JButton("Ir al Marketplace");
        
        btnIrMarketplace.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Abriendo Marketplace (Pendiente de implementar).", "Navegación", JOptionPane.INFORMATION_MESSAGE);
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridy = 0;
        panel.add(lblTitulo, gbc);
        
        gbc.gridy = 1;
        panel.add(btnIrMarketplace, gbc);

        return panel;
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
