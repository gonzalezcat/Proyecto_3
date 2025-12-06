package boletamaster.marketplace;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Usuario;
import java.io.Serializable;

public class Oferta implements Serializable {
    private final String id;
    private final Ticket ticket;
    private final Usuario vendedor;
    private double precioPublico; 
    private boolean activa;
    private final LocalDateTime fechaCreacion;
    private final List<ContraOferta> contraOfertas;
    private static final long serialVersionUID = 1L;


    public Oferta(Ticket ticket, Usuario vendedor, double precioPublico) {
        this.id = java.util.UUID.randomUUID().toString();
        this.ticket = ticket;
        this.vendedor = vendedor;
        this.precioPublico = precioPublico;
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
        this.contraOfertas = new ArrayList<>();
    }

    public String getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public Usuario getVendedor() { return vendedor; }
    public double getPrecioPublico() { return precioPublico; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean a) { this.activa = a; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<ContraOferta> getContraOfertas() { return contraOfertas; }

    public void agregarContraOferta(ContraOferta co) {
        contraOfertas.add(co);
    }
    
    
    public double getPrecioActual() {
        if (!contraOfertas.isEmpty()) {
            return contraOfertas.get(contraOfertas.size() - 1).getPrecio();
        }
        return precioPublico;
    }

    public static class ContraOferta {
        private final Usuario comprador;
        private final double precio;
        private final LocalDateTime fecha;

        public ContraOferta(Usuario comprador, double precio) {
            this.comprador = comprador;
            this.precio = precio;
            this.fecha = LocalDateTime.now();
        }

        public Usuario getComprador() { return comprador; }
        public double getPrecio() { return precio; }
        public LocalDateTime getFecha() { return fecha; }
    }
}