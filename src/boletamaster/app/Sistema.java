package boletamaster.app;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.eventos.Venue;
import boletamaster.persistence.SimpleRepository;
import boletamaster.tiquetes.Ticket;
import boletamaster.tiquetes.TicketDeluxe;
import boletamaster.tiquetes.TicketEstado;
import boletamaster.tiquetes.TicketMultiple;
import boletamaster.transacciones.Compra;
import boletamaster.transacciones.Reembolso;
import boletamaster.usuarios.Administrador;
import boletamaster.usuarios.Comprador;
import boletamaster.usuarios.Organizador;
import boletamaster.usuarios.Usuario;
import logica.BoletamasterSystem;

public class Sistema {

    private final BoletamasterSystem core;

    public Sistema(BoletamasterSystem core) {
        if (core == null) throw new IllegalArgumentException("Core system cannot be null.");
        this.core = core;
    }

   
    public void registrarUsuario(String login, String password, String nombre, String tipo) {
        if ("Comprador".equalsIgnoreCase(tipo)) {
             core.getGestorUsuarios().registrarComprador(login, password, nombre);
        } else if ("Organizador".equalsIgnoreCase(tipo)) {
             core.getGestorUsuarios().registrarOrganizador(login, password, nombre);
        } else if ("Administrador".equalsIgnoreCase(tipo)) {
             core.getGestorUsuarios().registrarAdministrador(login, password, nombre);
        } else {
             throw new IllegalArgumentException("Tipo de usuario inválido.");
        }
    }
    

    public Usuario buscarUsuarioPorLogin(String login) {
        return core.getGestorUsuarios().buscarUsuarioPorLogin(login).orElse(null);
    }
    

    public void registrarVenue(Venue v) {
        core.getGestorEventos().agregarVenue(v);
    }
    
    public Evento registrarEvento(Organizador organizador, String nombre, 
    		LocalDateTime fecha, Venue venue) {
    	Evento nuevoEvento = core.getGestorEventos().crearEvento(organizador, nombre, fecha, venue);
    	core.getRepo().guardarEventos(); 
    	return nuevoEvento;
    }
    
    public List<Evento> eventosActivosPorOrganizador(Organizador org) {
        return core.getGestorEventos().getEventosPorOrganizador(org); 
    }

    public Ticket generarTicketSimple(Localidad loc) {
        return core.getGestorTiquetes().crearTicketSimple(loc);
    }

    public Ticket generarTicketNumerado(Localidad loc, String asiento) {
        return core.getGestorTiquetes().crearTicketNumerado(loc, asiento);
    }

    public TicketMultiple generarTicketMultiple(Localidad loc, int cantidad, double precioPaquete) {
        return core.getGestorTiquetes().crearTicketMultiple(loc, cantidad, precioPaquete);
    }

    public TicketDeluxe generarTicketDeluxe(Localidad loc) {
        return core.getGestorTiquetes().crearTicketDeluxe(loc);
    }

    public Compra comprarTicket(Usuario comprador, Ticket t) {
        if (!(comprador instanceof Comprador)) 
            throw new IllegalArgumentException("El usuario no es un comprador válido");
        return core.getGestorVentas().procesarCompra((Comprador) comprador, Collections.singletonList(t), true);
    }

    public void transferirTicket(Ticket t, Usuario actual, String password, Usuario nuevo) {
        core.getGestorVentas().transferirTicket(t, actual, password, nuevo);
    }

    public List<Reembolso> cancelarEventoYReembolsar(Evento e, Administrador admin) {
        List<Reembolso> resultado = new ArrayList<>();
        // ... (Lógica de reembolso se mantiene) ...
        for (Ticket t : core.getRepo().getTickets()) {
            if (t.getEvento() != null && t.getEvento().equals(e) && t.getPropietario() != null) {
                // Calculation of refund amount (precio base + service fee)
                double monto = t.getPrecioBase() + (t.getPrecioBase() * t.getPorcentajeServicio());
                t.getPropietario().depositarSaldo(monto);
                Reembolso r = new Reembolso(t.getPropietario(), monto);
                core.getRepo().addTransaccion(r);
                resultado.add(r);
                t.setEstado(TicketEstado.CANCELADO);
            }
        }
        return resultado;
    }

    public void reporteFinancieroPorOrganizador(Organizador org) {
        core.getReporteador().generarReportePorOrganizador(org);
    }

    public SimpleRepository getRepo() {
        return core.getRepo();
    }

    public void setCuotaFijaGlobal(double cuotaFijaGlobal) {
        core.getGestorFinanzas().setCuotaFijaGlobal(cuotaFijaGlobal);
    }

    public void setPorcentajeServicioGlobal(double porcentajeServicioGlobal) {
        core.getGestorFinanzas().setPorcentajeServicioGlobal(porcentajeServicioGlobal);
    }
    
    public Localidad buscarLocalidad(String nombre) {
        if (nombre == null || nombre.isEmpty()) return null;

        for (Evento e : core.getGestorEventos().getEventos()) { 
            for (Localidad loc : e.getLocalidades()) { 
                if (loc.getNombre().equalsIgnoreCase(nombre)) {
                    return loc;
                }
            }
        }
        return null; 
    }

    public Evento buscarEventoPorIdYOrganizador(String idEvento, Organizador org) {
        
        for (Evento e : core.getGestorEventos().getEventosPorOrganizador(org)) {
            if (e.getId().equals(idEvento)) {
                return e;
            }
        }
        return null; 
    }
    public List<Ticket> obtenerTicketsPorPropietario(Comprador comprador) {
        if (comprador == null) {
            return Collections.emptyList();
        }
        return core.getRepo().getTickets().stream()
                .filter(t -> t.getPropietario() != null && t.getPropietario().equals(comprador))
                .collect(Collectors.toList());
    }
    
    public Ticket buscarTicketGlobalPorId(String idTicket) {
        if (idTicket == null || idTicket.isEmpty()) {
            return null;
        }
        return core.getRepo().getTickets().stream()
                .filter(t -> t.getId().equals(idTicket))
                .findFirst()
                .orElse(null);
    }
    
    public void setEventoPublicado(Evento e, boolean publicado) {
        if (e == null) throw new IllegalArgumentException("Evento nulo.");
        e.setPublicado(publicado); 
        core.getRepo().guardarEventos();
    }
    
    public void crearLocalidadYGenerarTickets(Evento evento, String nombre, double precio, int capacidad, boolean esNumerada) {
        if (evento == null) {
            throw new IllegalArgumentException("El evento no puede ser nulo.");
        }
        if (nombre == null || nombre.trim().isEmpty() || precio <= 0 || capacidad <= 0) {
            throw new IllegalArgumentException("Datos de localidad inválidos (nombre, precio o capacidad).");
        }
        Localidad nuevaLocalidad = new Localidad(UUID.randomUUID().toString(), nombre, precio, capacidad, esNumerada);
        evento.addLocalidad(nuevaLocalidad); 
        
        for (int i = 0; i < capacidad; i++) {
            if (esNumerada) {
                String asiento = nombre.substring(0, 1).toUpperCase() + String.format("%03d", i + 1);
                
                generarTicketNumerado(nuevaLocalidad, asiento); 
            } else {
                generarTicketSimple(nuevaLocalidad);
            }
        }

        core.getRepo().guardarEventos();
    	core.getRepo().guardarTickets(); 

    }

    public BoletamasterSystem getCore() {
    	return core;
    }
}