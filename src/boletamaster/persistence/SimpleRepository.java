package boletamaster.persistence;

import java.io.Serializable;
import java.util.List;

import boletamaster.eventos.Evento;
import boletamaster.eventos.Venue;
import boletamaster.marketplace.LogRegistro;
import boletamaster.marketplace.Oferta;
import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Usuario;

public class SimpleRepository implements Serializable {
	private static final long serialVersionUID = 1L;


    private final List<Usuario> usuarios;
    private final List<Venue> venues;
    private final List<Evento> eventos;
    private final List<Ticket> tickets;
    private final List<Object> transacciones;
    private final List<Oferta> ofertas;
    private final List<LogRegistro> log;

    public SimpleRepository() {
        this.usuarios = DataManager.cargarLista(PersistenceConfig.USERS_FILE);
        this.venues = DataManager.cargarLista(PersistenceConfig.VENUES_FILE);
        this.eventos = DataManager.cargarLista(PersistenceConfig.EVENTOS_FILE);
        this.tickets = DataManager.cargarLista(PersistenceConfig.TICKETS_FILE);
        this.transacciones = DataManager.cargarLista(PersistenceConfig.TRANSACCIONES_FILE);
        this.ofertas = DataManager.cargarLista(PersistenceConfig.MARKET_FILE);
        this.log = DataManager.cargarLista(PersistenceConfig.LOG_FILE);
    }

    public List<Usuario> getUsuarios() { return usuarios; }
    public List<Venue> getVenues() { return venues; }
    public List<Evento> getEventos() { return eventos; }
    public List<Ticket> getTickets() { return tickets; }
    public List<Object> getTransacciones() { return transacciones; }
    public List<Oferta> getOfertas() { return ofertas; }
    public List<LogRegistro> getLog() { return log; }

    public void addUsuario(Usuario u) {
        usuarios.add(u);
        DataManager.guardarLista(PersistenceConfig.USERS_FILE, usuarios);
    }

    public void addVenue(Venue v) {
        venues.add(v);
        DataManager.guardarLista(PersistenceConfig.VENUES_FILE, venues);
    }

    public void addEvento(Evento e) {
        eventos.add(e);
        DataManager.guardarLista(PersistenceConfig.EVENTOS_FILE, eventos);
    }

    public void addTicket(Ticket t) {
        tickets.add(t);
        DataManager.guardarLista(PersistenceConfig.TICKETS_FILE, tickets);
    }

    public void addTransaccion(Object o) {
        transacciones.add(o);
        DataManager.guardarLista(PersistenceConfig.TRANSACCIONES_FILE, transacciones);
    }

    public void addOferta(Oferta oferta) {
        ofertas.add(oferta);
        DataManager.guardarLista(PersistenceConfig.MARKET_FILE, ofertas);
    }

    public void addLog(LogRegistro l) {
        log.add(l);
        DataManager.guardarLista(PersistenceConfig.LOG_FILE, log);
    }
}



