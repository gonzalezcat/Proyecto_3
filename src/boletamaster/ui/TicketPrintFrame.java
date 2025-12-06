package boletamaster.ui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.google.zxing.WriterException;

import boletamaster.tiquetes.Ticket;
import boletamaster.util.QrCodeGenerator;


public class TicketPrintFrame extends JFrame {
	private static final long serialVersionUID = 1L;

    
    private final Ticket ticket;
    
    public TicketPrintFrame(Ticket ticket) {
        this.ticket = ticket;
        setTitle("Impresión de Tiquete - ID: " + ticket.getId().substring(0, 8));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 600);
        setLocationRelativeTo(null);
        
        if (ticket.getEstado().equals("IMPRESO")) {
            JOptionPane.showMessageDialog(this, "Advertencia: Este tiquete ya fue impreso y esta bloqueado para futuras impresiones.", "Impresion Bloqueada", JOptionPane.WARNING_MESSAGE);
        }

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        mainPanel.add(createTicketInfoPanel(), BorderLayout.NORTH);
        mainPanel.add(createQrCodePanel(), BorderLayout.CENTER);
        
        add(mainPanel);
    }

    private JPanel createTicketInfoPanel() {
        JPanel infoPanel = new JPanel(new GridLayout(6, 1));
        
        LocalDateTime now = LocalDateTime.now();
        String fechaImpresion = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        infoPanel.add(new JLabel("<html><h1>BOLETAMASTER</h1></html>"));
        infoPanel.add(new JLabel("<html><b>Evento:</b> " + ticket.getEvento().getNombre() + "</html>"));
        infoPanel.add(new JLabel("<html><b>ID Ticket:</b> " + ticket.getId() + "</html>"));
        infoPanel.add(new JLabel("<html><b>Fecha Evento:</b> " + ticket.getEvento().getFechaHora().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME) + "</html>"));
        infoPanel.add(new JLabel("<html><b>Localidad:</b> " + ticket.getLocalidad().getNombre() + "</html>"));
        infoPanel.add(new JLabel("<html><b>Fecha Impresión:</b> " + fechaImpresion + "</html>"));
        
        return infoPanel;
    }

    private JPanel createQrCodePanel() {
        JPanel qrPanel = new JPanel(new BorderLayout());
        qrPanel.setBorder(BorderFactory.createTitledBorder("Código QR de Acceso"));
        
        boolean alreadyImpreso = ticket.isImpreso(); 

        if (alreadyImpreso) {
            qrPanel.add(new JLabel("Tiquete ya impreso y bloqueado."), BorderLayout.CENTER);
            return qrPanel;
        }
        
        String qrContent = buildQrContent();
        
        try {
            int qrSize = 300;
            BufferedImage qrImage = QrCodeGenerator.generate(qrContent, qrSize, qrSize);
            
            JLabel qrLabel = new JLabel(new ImageIcon(qrImage)); 
            qrLabel.setHorizontalAlignment(SwingConstants.CENTER);
            
            qrPanel.add(qrLabel, BorderLayout.CENTER);
            
            try {
                ticket.marcarComoImpreso(); 
                JOptionPane.showMessageDialog(this, "¡Impresión Exitosa! Tiquete marcado como IMPRESO y bloqueado para reventa/transferencia.", "Estado Actualizado", JOptionPane.INFORMATION_MESSAGE);
            } catch (IllegalStateException e) {
                 JOptionPane.showMessageDialog(this, "Error de estado al marcar como impreso: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }

        } catch (WriterException | IllegalArgumentException e) {
             qrPanel.add(new JLabel("Error al generar QR: " + e.getMessage()), BorderLayout.CENTER);
        }
        
        return qrPanel;
    }
    
    private String buildQrContent() {
        LocalDateTime now = LocalDateTime.now();
        return String.format("%s|%s|%s|%s",
            ticket.getEvento().getNombre(),
            ticket.getId(),
            ticket.getEvento().getFechaHora().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            now.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }
}