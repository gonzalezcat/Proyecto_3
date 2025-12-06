package boletamaster.usuarios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import boletamaster.app.Sistema;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Venue;
import boletamaster.tiquetes.Ticket;
import boletamaster.tiquetes.TicketEstado;
import boletamaster.tiquetes.TicketMultiple;

public class Organizador extends Usuario {
	private static final long serialVersionUID = 1L;

    public Organizador(String login, String password, String nombre) {
        super(login, password, nombre);
    }

    public List<Evento> eventosActivos(Sistema sistema) {
        if (sistema == null) throw new IllegalArgumentException("Sistema nulo");
        List<Evento> res = new ArrayList<>();
        for (Object o : sistema.getRepo().getEventos()) {
            if (!(o instanceof Evento)) continue;
            Evento e = (Evento) o;
            if (e.getOrganizador().getLogin().equals(this.getLogin()) && !e.isCancelado() && e.getFechaHora().isAfter(java.time.LocalDateTime.now())) {
                res.add(e);
            }
        }
        return res;
    }

   
    public Venue venueEvento(Evento e) {
        if (e == null) throw new IllegalArgumentException("Evento nulo");
        if (!e.getOrganizador().getLogin().equals(this.getLogin())) throw new IllegalStateException("No es el organizador de este evento");
        return e.getVenue();
    }

   
    public Map<String,Integer> cantidadTipoTickets(Evento e) {
        if (e == null) throw new IllegalArgumentException("Evento nulo");
        if (!e.getOrganizador().getLogin().equals(this.getLogin())) throw new IllegalStateException("No es el organizador de este evento");

        Map<String,Integer> cuenta = new HashMap<>();
        cuenta.put("SIMPLE", 0);
        cuenta.put("NUMERADO", 0);
        cuenta.put("MULTIPLE", 0);
        cuenta.put("DELUXE", 0);

        for (Ticket t : e.getTickets()) {
            if (t instanceof TicketMultiple) {
                cuenta.put("MULTIPLE", cuenta.get("MULTIPLE") + 1);
            } else if (t instanceof boletamaster.tiquetes.TicketNumerado) {
                cuenta.put("NUMERADO", cuenta.get("NUMERADO") + 1);
            } else if (t instanceof boletamaster.tiquetes.TicketDeluxe) {
                cuenta.put("DELUXE", cuenta.get("DELUXE") + 1);
            } else {
                cuenta.put("SIMPLE", cuenta.get("SIMPLE") + 1);
            }
        }
        return cuenta;
    }

    public void reservarTicketParaInvitado(Evento e, Ticket t, Usuario invitado) {
        if (e == null || t == null || invitado == null) throw new IllegalArgumentException("Argumento nulo");
        if (!e.getOrganizador().getLogin().equals(this.getLogin())) throw new IllegalStateException("No es el organizador del evento");
        if (!t.getEvento().getId().equals(e.getId())) throw new IllegalArgumentException("Ticket no pertenece al evento");
        if (t.ticketVencido()) throw new IllegalStateException("Ticket vencido");
        if (t.getEstado() != TicketEstado.DISPONIBLE) throw new IllegalStateException("Ticket no disponible");

        t.propietario = invitado;
        t.setEstado(TicketEstado.VENDIDO);
    }

    @Override
    public String toString() {
        return "Organizador{" + "login=" + login + ", nombre=" + nombre + '}';
    }
}
