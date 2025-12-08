package logica;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import boletamaster.app.Sistema;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.eventos.Venue;
import boletamaster.marketplace.Marketplace;
import boletamaster.persistence.SimpleRepository;
import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Comprador;
import boletamaster.usuarios.Organizador;

public class BoletamasterSystem {

    private static BoletamasterSystem instance;

    private final SimpleRepository repo;
    private final Marketplace marketplace;
    
    private final GestorFinanzas gestorFinanzas;
    private final GestorVentas gestorVentas;
    private final GestorTiquetes gestorTiquetes;
    private final Reporteador reporteador;
    
    private final GestorPersistencia gestorPersistencia;
    private final GestorEventos gestorEventos;
    private final GestorUsuarios gestorUsuarios;
    private final GestorReembolsos gestorReembolsos;    
 
    private BoletamasterSystem() {
        this.repo = new SimpleRepository();

        Sistema facade = new Sistema(this);
        
        this.gestorPersistencia = new GestorPersistencia(facade);
        
        this.gestorFinanzas = new GestorFinanzas(facade);
        this.gestorVentas = new GestorVentas(facade, gestorFinanzas);
        this.gestorTiquetes = new GestorTiquetes(facade);
        this.reporteador = new Reporteador(facade, gestorFinanzas);
        this.gestorReembolsos = new GestorReembolsos(facade);
        this.marketplace = new Marketplace(facade);
        
        this.gestorEventos = new GestorEventos(facade); 
        this.gestorUsuarios = new GestorUsuarios(facade); 
        
        if (repo.getUsuarios().isEmpty()) {
            cargarDatosIniciales();
        }
    }

    public static BoletamasterSystem getInstance() {
        if (instance == null) {
            instance = new BoletamasterSystem();
        }
        return instance;
    }

    public static void resetInstance() { 
        instance = null; 
    }

    
    public SimpleRepository getRepo() { return repo; }
    public Marketplace getMarketplace() { return marketplace; }
    public GestorFinanzas getGestorFinanzas() { return gestorFinanzas; }
    public GestorVentas getGestorVentas() { return gestorVentas; }
    public GestorTiquetes getGestorTiquetes() { return gestorTiquetes; }
    public Reporteador getReporteador() { return reporteador; }
    public GestorEventos getGestorEventos() { return gestorEventos; }
    public GestorUsuarios getGestorUsuarios() { return gestorUsuarios; }
    public GestorReembolsos getGestorReembolsos() { return gestorReembolsos; }
    public GestorPersistencia getGestorPersistencia() {return gestorPersistencia; }

    
    private void cargarDatosIniciales() {
        System.out.println("Cargando datos iniciales de prueba...");
        
        getGestorUsuarios().registrarComprador("juanito", "123", "Juan Pérez");
        getGestorUsuarios().registrarOrganizador("live_nation", "456", "Live Nation Corp.");
        getGestorUsuarios().registrarAdministrador("admin", "789", "Super Admin");
        
        Optional<Organizador> optOrg = getGestorUsuarios().buscarUsuarioPorLogin("live_nation")
                                                         .filter(u -> u instanceof Organizador)
                                                         .map(u -> (Organizador) u);
        
        Optional<Comprador> optComprador = getGestorUsuarios().buscarUsuarioPorLogin("juanito")
                                                              .filter(u -> u instanceof Comprador)
                                                              .map(u -> (Comprador) u);
                                                              
        Organizador org = optOrg.orElse(null);
        Comprador comprador = optComprador.orElse(null);
        
        if (org == null) {
            System.err.println("❌ Error: No se pudo encontrar el Organizador 'live_nation'.");
            return;
        }
        
        Venue v1 = new Venue("V001", "Estadio Azteca", "Ciudad de México", 87000, true);
        Venue v2 = new Venue("V002", "Movistar Arena", "Bogotá", 14000, true);
        
        getGestorEventos().agregarVenue(v1); 
        getGestorEventos().agregarVenue(v2);
        
        LocalDateTime date1 = LocalDateTime.of(2025, 12, 10, 20, 0);
        LocalDateTime date2 = LocalDateTime.of(2026, 2, 25, 14, 0);
        
        Evento e1 = getGestorEventos().crearEvento(org, "Concierto de Shakira", date1, v1);
        Evento e2 = getGestorEventos().crearEvento(org, "Festival Rock Al Parque", date2, v2);

        List<Ticket> ticketsE1 = new ArrayList<>();
        Localidad loc1a = new Localidad("L1A", "VIP", 500.00, 500, false);
        Localidad loc1b = new Localidad("L1B", "General", 150.00, 5000, false);
        e1.addLocalidad(loc1a);
        e1.addLocalidad(loc1b);
        
        ticketsE1.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e1, loc1a, 500));
        ticketsE1.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e1, loc1b, 5000));
        
        List<Ticket> ticketsE2 = new ArrayList<>();
        Localidad loc2a = new Localidad("L2A", "Platea", 120.00, 2000, false);
        Localidad loc2b = new Localidad("L2B", "Gradería", 50.00, 5000, false);
        e2.addLocalidad(loc2a);
        e2.addLocalidad(loc2b);

        ticketsE2.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e2, loc2a, 2000));
        ticketsE2.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e2, loc2b, 5000));

        
        for (Ticket t : ticketsE1) { t.setEvento(e1); getRepo().addTicket(t); }
        for (Ticket t : ticketsE2) { t.setEvento(e2); getRepo().addTicket(t); }
        
        if (comprador != null && !ticketsE1.isEmpty()) {
            Ticket ticketToSell = ticketsE1.get(0); 
            getGestorTiquetes().venderTicket(ticketToSell, comprador, "Tarjeta de Prueba", "123"); 
            comprador.depositarSaldo(1000.00); 
            
            getRepo().guardarUsuarios(); 
            getRepo().guardarTickets(); 
            
            System.out.println("Ticket " + ticketToSell.getId() + " vendido y asignado a Juan Pérez. Saldo inicial: 1000.00");
        }
        
        System.out.println("Carga de datos inicial completa. Listos para usar.");
    }
    
    @Override
    public String toString() {
        int numUsuarios = getGestorUsuarios().getUsuarios().size();
        int numEventos = getGestorEventos().getEventos().size();
        int numVenues = getGestorEventos().getVenues().size();
        
        int numTickets = repo.getTickets().size(); 

        return "BoletamasterSystem {" +
                "usuarios=" + numUsuarios +
                ", eventos=" + numEventos +
                ", venues=" + numVenues +
                ", tickets=" + numTickets +
                '}';
    }
}