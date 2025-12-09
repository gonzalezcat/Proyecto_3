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
        this.gestorPersistencia.cargarTodo();
        this.gestorFinanzas = new GestorFinanzas(facade);
        this.gestorVentas = new GestorVentas(facade, gestorFinanzas);
        this.gestorTiquetes = new GestorTiquetes(facade);
        this.reporteador = new Reporteador(facade, gestorFinanzas);
        this.gestorReembolsos = new GestorReembolsos(facade);
        this.marketplace = new Marketplace(facade);
        this.gestorEventos = new GestorEventos(facade); 
        this.gestorUsuarios = new GestorUsuarios(facade); 
        this.cargarDatosIniciales();

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

        Organizador org = (Organizador) getGestorUsuarios()
                .buscarUsuarioPorLogin("live_nation").orElse(null);

        Comprador comprador = (Comprador) getGestorUsuarios()
                .buscarUsuarioPorLogin("juanito").orElse(null);

        if (org == null) {
            System.err.println("❌ No existe organizador 'live_nation'.");
            return;
        }

        Venue v1 = new Venue("V001", "Estadio Azteca", "Ciudad de México", 87000, true);
        Venue v2 = new Venue("V002", "Movistar Arena", "Bogotá", 14000, true);

        getGestorEventos().agregarVenue(v1);
        getGestorEventos().agregarVenue(v2);

        Evento e1 = getGestorEventos().crearEvento(
                org, "Concierto de Shakira",
                LocalDateTime.of(2025, 12, 10, 20, 0), v1
        );

        Evento e2 = getGestorEventos().crearEvento(
                org, "Festival Rock Al Parque",
                LocalDateTime.of(2026, 2, 25, 14, 0), v2
        );

        // --- LOCALIDADES ---
        Localidad loc1a = new Localidad("L1A", "VIP", 500.00, 500, false);
        Localidad loc1b = new Localidad("L1B", "General", 150.00, 5000, false);

        e1.addLocalidad(loc1a);
        e1.addLocalidad(loc1b);

        Localidad loc2a = new Localidad("L2A", "Platea", 120.00, 2000, false);
        Localidad loc2b = new Localidad("L2B", "Gradería", 50.00, 5000, false);

        e2.addLocalidad(loc2a);
        e2.addLocalidad(loc2b);

        List<Ticket> ticketsE1 = new ArrayList<>();
        ticketsE1.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e1, loc1a, 500));
        ticketsE1.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e1, loc1b, 5000));

        List<Ticket> ticketsE2 = new ArrayList<>();
        ticketsE2.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e2, loc2a, 2000));
        ticketsE2.addAll(getGestorTiquetes().generarTicketsParaLocalidad(e2, loc2b, 5000));

        ticketsE1.forEach(t -> getRepo().addTicket(t));
        ticketsE2.forEach(t -> getRepo().addTicket(t));

        if (comprador != null && !ticketsE1.isEmpty()) {
            Ticket t = ticketsE1.get(0);

            getGestorTiquetes().venderTicket(
                    t, comprador,
                    "Tarjeta Prueba", "123"
            );

            comprador.depositarSaldo(1000.00);

            getRepo().guardarUsuarios();
            getRepo().guardarTickets();

            System.out.println("Ticket vendido a Juan Pérez. Saldo 1000.");
        }

        System.out.println("Datos iniciales cargados correctamente.");
        
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