package boletamaster.tiquetes;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;

public class TicketDeluxe extends Ticket {
	private static final long serialVersionUID = 1L;

    
    public TicketDeluxe(Evento evento, Localidad localidad, double precioBase, 
                       double porcentajeServicio, double cuotaFija) {
        super(evento, localidad, precioBase, porcentajeServicio, cuotaFija);
    }

    @Override
    public boolean esTransferible() {
        return false; 
    }
}
