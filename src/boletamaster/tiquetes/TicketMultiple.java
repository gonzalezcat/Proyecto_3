package boletamaster.tiquetes;
import java.util.ArrayList;
import java.util.List;

import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.usuarios.Usuario;

public class TicketMultiple extends Ticket {
	private static final long serialVersionUID = 1L;
    private final List<Ticket> elementos; 
    private boolean paqueteTransferido; 

    public TicketMultiple(Evento evento, Localidad localidad, double precioBase, 
            double porcentajeServicio, double cuotaFija) {
        super(evento, localidad, precioBase, porcentajeServicio, cuotaFija);
        this.elementos = new ArrayList<>();
        this.paqueteTransferido = false;
    }

    public void addElemento(Ticket t) {
        if (t == null) throw new IllegalArgumentException("Elemento nulo");
        elementos.add(t);
    }

    public List<Ticket> getElementos() { return elementos; }

    @Override
    public boolean esTransferible() {
        
        if (paqueteTransferido) return false;
        for (Ticket t : elementos) {
            if (t.getEstado() == TicketEstado.TRANSFERIDO) return false;
        
            if (t.getEstado() == TicketEstado.USADO) return false;
        }
        return true;
    }

    @Override
    public void transferirA(Usuario nuevoPropietario, Usuario actual, String passwordDelActual) {
        super.transferirA(nuevoPropietario, actual, passwordDelActual);
       
        for (Ticket t : elementos) {
            t.propietario = nuevoPropietario;
            t.setEstado(TicketEstado.TRANSFERIDO);
        }
        this.paqueteTransferido = true;
    }

   
    public int ticketsMultiples() {
        return elementos.size();
    }
}

