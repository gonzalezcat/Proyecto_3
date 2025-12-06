package boletamaster.transacciones;

import java.util.ArrayList;
import java.util.List;

import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Usuario;

public class Compra extends Transaccion {
	private static final long serialVersionUID = 1L;

    private final List<Ticket> tickets;  

    public Compra(Usuario usuario, double montoTotal) {
        super(usuario, montoTotal);
        this.tickets = new ArrayList<>();
    }

    public List<Ticket> getTickets() { 
        return new ArrayList<>(tickets); 
    }

    @Override
    public String toString() {
        return "Compra{" + getId() + ", usuario=" + usuario.getLogin() + 
               ", monto=" + montoTotal + ", tickets=" + tickets.size() + "}";
    }
}
