package boletamaster.persistence;

import java.io.Serializable;
import java.util.ArrayList; // Necesario para inicializar en caso de reinicio
import java.util.List;
import java.io.File; // Necesario para la función de borrado

import boletamaster.eventos.Evento;
import boletamaster.eventos.Venue;
import boletamaster.marketplace.LogRegistro;
import boletamaster.marketplace.Oferta;
import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Usuario;

public class SimpleRepository implements Serializable {
    private static final long serialVersionUID = 1L;

    private List<Usuario> usuarios;
    private List<Venue> venues;
    private List<Evento> eventos;
    private List<Ticket> tickets;
    private List<Object> transacciones;
    private List<Oferta> ofertas;
    private List<LogRegistro> log;

    public SimpleRepository() {
        inicializarRepositorio();
    }
    
    
    private void inicializarRepositorio() {
        this.usuarios = DataManager.cargarLista(PersistenceConfig.USERS_FILE);
        this.venues = DataManager.cargarLista(PersistenceConfig.VENUES_FILE);
        this.eventos = DataManager.cargarLista(PersistenceConfig.EVENTOS_FILE);
        this.tickets = DataManager.cargarLista(PersistenceConfig.TICKETS_FILE);
        this.transacciones = DataManager.cargarLista(PersistenceConfig.TRANSACCIONES_FILE);
        this.ofertas = DataManager.cargarLista(PersistenceConfig.MARKET_FILE);
        this.log = DataManager.cargarLista(PersistenceConfig.LOG_FILE);
    }

    
    public void inicializarRepositorioVacio() {
        this.usuarios = new ArrayList<>();
        this.venues = new ArrayList<>();
        this.eventos = new ArrayList<>();
        this.tickets = new ArrayList<>();
        this.transacciones = new ArrayList<>();
        this.ofertas = new ArrayList<>();
        this.log = new ArrayList<>();
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
    
    
    public void guardarEventos() {
        DataManager.guardarLista(PersistenceConfig.EVENTOS_FILE, eventos);
    }

    
    public void guardarTickets() {
        DataManager.guardarLista(PersistenceConfig.TICKETS_FILE, tickets);
    }
    
    public void guardarUsuarios() {
        DataManager.guardarLista(PersistenceConfig.USERS_FILE, usuarios);
    }

   
    public void borrarArchivosDeDatos() {
        String dataFolderPath = PersistenceConfig.DATA_FOLDER; 
        File dataFolder = new File(dataFolderPath);
        
        if (dataFolder.exists() && dataFolder.isDirectory()) {
            boolean allDeleted = true;
            for (File file : dataFolder.listFiles()) {
                if (!file.delete()) {
                    System.err.println("Error al borrar el archivo: " + file.getName());
                    allDeleted = false;
                }
            }
            if (allDeleted) {
                if (dataFolder.delete()) {
                    System.out.println("Carpeta de datos borrada: " + dataFolderPath);
                } else {
                    System.err.println("La carpeta de datos no pudo ser borrada: " + dataFolderPath);
                }
            }
        } else {
            System.out.println("La carpeta de datos no existe. No hay nada que borrar.");
        }
    }
}


