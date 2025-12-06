package boletamaster.eventos;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import boletamaster.tiquetes.Ticket;
import boletamaster.tiquetes.TicketSimple;
import boletamaster.usuarios.Usuario;

public class Oferta implements Serializable{

    private final String id;
    private final Ticket ticket;
    private final Usuario vendedor;
    private double precioPublico;
    private boolean activa;
    private final LocalDateTime fechaCreacion;
    private final LocalDateTime inicio;
    private final LocalDateTime fin;
    private final double porcentajeDescuento;
    private final List<ContraOferta> contraOfertas;
    private static final long serialVersionUID = 1L;

   
    public Oferta(Ticket ticket, Usuario vendedor, double precioPublico,
                  double porcentajeDescuento, LocalDateTime inicio, LocalDateTime fin) {
        this.id = java.util.UUID.randomUUID().toString();
        this.ticket = ticket;
        this.vendedor = vendedor;
        this.precioPublico = precioPublico;
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
        this.porcentajeDescuento = porcentajeDescuento;
        this.inicio = inicio;
        this.fin = fin;
        this.contraOfertas = new ArrayList<>();
    }

    // Constructor usado por Marketplace con solo Localidad y porcentaje
    public Oferta(Localidad loc, double porcentajeDescuento, LocalDateTime inicio, LocalDateTime fin) {
        this.id = java.util.UUID.randomUUID().toString();
        this.ticket = new TicketSimple(loc); // ticket concreto
        this.vendedor = null;                 
        this.precioPublico = 0.0;
        this.activa = true;
        this.fechaCreacion = LocalDateTime.now();
        this.porcentajeDescuento = porcentajeDescuento;
        this.inicio = inicio;
        this.fin = fin;
        this.contraOfertas = new ArrayList<>();
    }

    public String getId() { return id; }
    public Ticket getTicket() { return ticket; }
    public Usuario getVendedor() { return vendedor; }
    public double getPrecioPublico() { return precioPublico; }
    public void setPrecioPublico(double p) { this.precioPublico = p; }
    public boolean isActiva() { return activa; }
    public void setActiva(boolean a) { this.activa = a; }
    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public List<ContraOferta> getContraOfertas() { return contraOfertas; }

    public void agregarContraOferta(ContraOferta co) {
        contraOfertas.add(co);
    }

    public Localidad getLocalidad() {
        return ticket.getLocalidad();
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public boolean estaVigente() {
        LocalDateTime now = LocalDateTime.now();
        return activa && (inicio == null || !now.isBefore(inicio)) && (fin == null || !now.isAfter(fin));
    }
    
    public double aplicarDescuento(double precio) {
        return precio * (1.0 - porcentajeDescuento);
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

