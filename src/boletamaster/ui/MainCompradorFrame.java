package boletamaster.ui;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Comprador;

public class MainCompradorFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    
    private final Sistema sistema;
    private final Comprador comprador;

    public MainCompradorFrame(Sistema sistema, Comprador comprador) {
        this.sistema = sistema;
        this.comprador = comprador;

        setTitle("BoletaMaster - Comprador: " + comprador.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Comprar Tickets", createDummyPanel("Panel para buscar eventos y comprar tickets"));

        tabbedPane.addTab("Mis Tickets", createMisTicketsPanel());

        tabbedPane.addTab("Marketplace", createDummyPanel("Panel para ver/publicar ofertas de reventa"));

        tabbedPane.addTab("Mi Cuenta", createCuentaPanel());
        
        add(tabbedPane, BorderLayout.CENTER);
        
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Archivo");
        JMenuItem logoutItem = new JMenuItem("Cerrar Sesión");
        
        logoutItem.addActionListener(e -> logout());
        fileMenu.add(logoutItem);
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }
    
    private JPanel createDummyPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCuentaPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JLabel saldoLabel = new JLabel("Saldo Actual: $" + String.format("%.2f", comprador.getSaldo()), SwingConstants.CENTER);
        saldoLabel.setFont(new Font("Arial", Font.BOLD, 20));
        
        JButton recargarButton = new JButton("Recargar Saldo (Simulado)");
        recargarButton.addActionListener(e -> {
            try {
                String montoStr = JOptionPane.showInputDialog(this, "Ingrese el monto a recargar:", "Recargar Saldo", JOptionPane.PLAIN_MESSAGE);
                if (montoStr != null) {
                    double monto = Double.parseDouble(montoStr);
                    comprador.depositarSaldo(monto);
                    saldoLabel.setText("Saldo Actual: $" + String.format("%.2f", comprador.getSaldo()));
                    JOptionPane.showMessageDialog(this, "Recarga exitosa.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                }
            } catch (NumberFormatException ex) {
                 JOptionPane.showMessageDialog(this, "Monto inválido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        panel.add(saldoLabel);
        panel.add(recargarButton);
        return panel;
    }
    
    private JPanel createMisTicketsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        JTextArea ticketListArea = new JTextArea("Lista de Tickets del Comprador (Implementación pendiente)");
        panel.add(new JScrollPane(ticketListArea), BorderLayout.CENTER);
        
        JButton printButton = new JButton("Imprimir Ticket Seleccionado");
        printButton.addActionListener(e -> openPrintDialog());
        
        panel.add(printButton, BorderLayout.SOUTH);
        
        
        return panel;
    }
    
    private void openPrintDialog() {
        if (!sistema.obtenerTicketsPorPropietario(comprador).isEmpty()) {
            new TicketPrintFrame(sistema.obtenerTicketsPorPropietario(comprador).get(0)).setVisible(true);
        } else {
             JOptionPane.showMessageDialog(this, "No tienes tickets para imprimir.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void logout() {
        this.dispose();
        new LoginFrame(sistema).setVisible(true);
    }
}