package logica;

import boletamaster.usuarios.Organizador;
import boletamaster.app.Sistema;
import boletamaster.eventos.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import boletamaster.usuarios.Administrador;

public class GestorEventos {
    
    private final Sistema sistema;  
    
    public GestorEventos(Sistema sistema) {
        this.sistema = sistema;
    }
    
    public void agregarVenue(Venue v) {
        if (v == null) throw new IllegalArgumentException("Venue nulo");
        sistema.getRepo().addVenue(v); 
    }
    
    public Evento crearEvento(Organizador organizador, String nombre, LocalDateTime fecha, Venue venue) {
        if (!venue.isAprobado()) {
            throw new IllegalStateException("Venue no aprobado: " + venue.getNombre());
        }
        if (fecha.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("La fecha del evento debe ser futura");
        }
        
        if (existeEventoEnVenueYFecha(venue, fecha)) { 
            throw new IllegalStateException("Ya hay un evento en " + venue.getNombre() + " para " + fecha.toLocalDate());
        }
        
        String idEvento = "EVT-" + System.currentTimeMillis();
        Evento nuevoEvento = new Evento(idEvento, nombre, fecha, venue, organizador);
        
        sistema.getRepo().addEvento(nuevoEvento);  
        
        return nuevoEvento;
    }
    
    private boolean existeEventoEnVenueYFecha(Venue venue, LocalDateTime fecha) {
        for (Evento eventoExistente : sistema.getRepo().getEventos()) { 
            boolean mismoVenue = eventoExistente.getVenue().equals(venue);
            boolean mismaFecha = eventoExistente.getFechaHora().toLocalDate().equals(fecha.toLocalDate());
            
            if (mismoVenue && mismaFecha) {
                return true;
            }
        }
        return false;
    }
    
    public List<Evento> getEventosPorOrganizador(Organizador organizador) {
        return sistema.getRepo().getEventos().stream()
                             .filter(e -> e.getOrganizador().equals(organizador))
                             .collect(Collectors.toList());
    }
    
    public boolean aprobarEvento(Administrador admin, Evento evento) {
        
        System.out.println("Evento '" + evento.getNombre() + "' aprobado por " + admin.getNombre());
        return true;
    }
    
    public List<Evento> getEventos() {
        return sistema.getRepo().getEventos();
    }
    
    public List<Venue> getVenues() {
        return sistema.getRepo().getVenues();
    }
}