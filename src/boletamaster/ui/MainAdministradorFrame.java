package boletamaster.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
import boletamaster.usuarios.Administrador;

public class MainAdministradorFrame extends JFrame {
	private static final long serialVersionUID = 1L;    
    private final Sistema sistema;
    private final Administrador administrador;

    public MainAdministradorFrame(Sistema sistema, Administrador administrador) {
        this.sistema = sistema;
        this.administrador = administrador;

        setTitle("BoletaMaster - Administrador: " + administrador.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Gestión de Eventos", createEventosAdminPanel());

        tabbedPane.addTab("Marketplace Admin", createMarketplaceAdminPanel());

        tabbedPane.addTab("Gestión de Usuarios", createDummyPanel("Panel para ver o gestionar cuentas de usuarios."));
        
        add(tabbedPane, BorderLayout.CENTER);
        
        setupMenuBar();
    }
    
    private JPanel createDummyPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel createEventosAdminPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton cancelarEventoButton = new JButton("Cancelar Evento y Procesar Reembolsos");

        cancelarEventoButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Cancelar/Reembolsar - Implementar UI."));
        
        panel.add(cancelarEventoButton);
        return panel;
    }

    private JPanel createMarketplaceAdminPanel() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        
        JButton verLogButton = new JButton("Ver Log de Transacciones");
        JButton eliminarOfertaButton = new JButton("Eliminar Oferta por ID");

        verLogButton.addActionListener(e -> {
            String log = sistema.getCore().getRepo().getLog().toString();
            JTextArea logArea = new JTextArea(log);
            logArea.setEditable(false);
            JScrollPane scrollPane = new JScrollPane(logArea);
            scrollPane.setPreferredSize(new Dimension(500, 300));
            JOptionPane.showMessageDialog(this, scrollPane, "Log del Sistema", JOptionPane.PLAIN_MESSAGE);
        });
        
        eliminarOfertaButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Eliminar Oferta - Implementar UI."));

        panel.add(verLogButton);
        panel.add(eliminarOfertaButton);
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