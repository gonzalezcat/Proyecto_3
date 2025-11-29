package boletamaster.eventos;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import boletamaster.usuarios.Organizador;
import boletamaster.tiquetes.Ticket;
import java.io.Serializable;

public class Evento implements Serializable {
    private final String id;
    private final String nombre;
    private final LocalDateTime fechaHora;
    private final Venue venue;
    private final Organizador organizador;
    private final List<Localidad> localidades;
    private final List<Ticket> tickets;
    private final List<Oferta> ofertas; //
    private boolean cancelado;
    private static final long serialVersionUID = 1L;

    public Evento(String id, String nombre, LocalDateTime fechaHora, Venue venue, Organizador organizador) {
        this.id = id;
        this.nombre = nombre;
        this.fechaHora = fechaHora;
        this.venue = venue;
        this.organizador = organizador;
        this.localidades = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.ofertas = new ArrayList<>(); // ✅ inicialización
        this.cancelado = false;
    }

    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public LocalDateTime getFechaHora() { return fechaHora; }
    public Venue getVenue() { return venue; }
    public Organizador getOrganizador() { return organizador; }
    public List<Localidad> getLocalidades() { return localidades; }
    public boolean isCancelado() { return cancelado; }
    public void cancelar() { this.cancelado = true; }

    public void addLocalidad(Localidad loc) { this.localidades.add(loc); }

    public void addTicket(Ticket t) {
        if (t == null) throw new IllegalArgumentException("Ticket nulo");
        tickets.add(t);
        t.setEvento(this);
    }

    public List<Ticket> getTickets() { return tickets; }

    // ✅ Nuevo método requerido por GestorVentas
    public List<Oferta> getOfertas() {
        return ofertas;
    }

    public void agregarOferta(Oferta o) {
        if (o != null) ofertas.add(o);
    }

    public int limiteTickets() {
        int total = 0;
        for (Localidad l : localidades) total += l.getCapacidad();
        return total;
    }

    @Override
    public String toString() {
        return "Evento{" + nombre + " en " + venue.getNombre() + " el " + fechaHora +
               ", organizador=" + organizador.getNombre() + "}";
    }
}
