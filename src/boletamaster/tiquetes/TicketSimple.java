package boletamaster.tiquetes;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;

public class TicketSimple extends Ticket {
	private static final long serialVersionUID = 1L;

    public TicketSimple(Evento evento, Localidad localidad,double precioBase, double porcentajeServicio, double cuotaFija) {
        super(evento, localidad,precioBase, porcentajeServicio, cuotaFija);
    }
    public TicketSimple(Localidad localidad) {
        super(null, localidad, 0.0, 0.0, 0.0);
    }

    @Override
    public boolean esTransferible() {
        return true;
    }
}