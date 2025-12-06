package boletamaster.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.SwingConstants;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Organizador;

public class MainOrganizadorFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    
    private final Sistema sistema;
    private final Organizador organizador;

    public MainOrganizadorFrame(Sistema sistema, Organizador organizador) {
        this.sistema = sistema;
        this.organizador = organizador;

        setTitle("BoletaMaster - Organizador: " + organizador.getNombre());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);

        JTabbedPane tabbedPane = new JTabbedPane();

        tabbedPane.addTab("Eventos Activos", createDummyPanel("Aqui se listaran y gestionarn los eventos del organizador."));

        tabbedPane.addTab("Crear Eventos/Venue", createCreacionPanel());

        tabbedPane.addTab("Reportes de Ventas", createDummyPanel("Panel para ver reportes de tickets vendidos y finanzas."));
        
        add(tabbedPane, BorderLayout.CENTER);
        
        setupMenuBar();
    }
    
    private JPanel createDummyPanel(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel(text, SwingConstants.CENTER), BorderLayout.CENTER);
        return panel;
    }

    private JPanel createCreacionPanel() {
        JPanel panel = new JPanel(new GridLayout(3, 1, 10, 10));
        
        JButton crearVenueButton = new JButton("Crear Nuevo Venue (Lugar)");
        JButton crearEventoButton = new JButton("Crear Nuevo Evento");
        JButton crearLocalidadButton = new JButton("Crear Localidad en Evento");
        
        crearVenueButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Crear Venue - Implementar UI."));
        crearEventoButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Crear Evento - Implementar UI."));
        crearLocalidadButton.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidad de Crear Localidad - Implementar UI."));
        
        panel.add(crearVenueButton);
        panel.add(crearEventoButton);
        panel.add(crearLocalidadButton);
        
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