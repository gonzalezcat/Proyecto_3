package boletamaster.tiquetes;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;

public class TicketNumerado extends Ticket {
	private static final long serialVersionUID = 1L;
    private final String asiento;
    
   
    public TicketNumerado(Evento evento, Localidad localidad, double precioBase, 
                         double porcentajeServicio, double cuotaFija, String asiento) {
        super(evento, localidad, precioBase, porcentajeServicio, cuotaFija);
        this.asiento = asiento;
    }

    public String getAsiento() { return asiento; }

    @Override
    public boolean esTransferible() {
        return true;
    }

    @Override
    public String toString() {
        return "TicketNumerado{evento=" + getEvento().getNombre() + 
               ", asiento=" + asiento + ", localidad=" + getLocalidad().getNombre() + 
               ", estado=" + getEstado() + "}";
    }
}