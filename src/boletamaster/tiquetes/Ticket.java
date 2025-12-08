package boletamaster.tiquetes;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.eventos.Oferta;
import boletamaster.usuarios.Usuario;

public abstract class Ticket implements Serializable{
    protected final String id;
    protected Evento evento;
    

	protected final Localidad localidad;
    protected final double precioBase;
    protected final double porcentajeServicio;
    protected final double cuotaFija;
    protected TicketEstado estado;
    public Usuario propietario;
    private static final long serialVersionUID = 1L;
    protected boolean impreso = false;

    public Ticket(Evento evento, Localidad localidad, double precioBase, double porcentajeServicio, double cuotaFija) {
    	this.id = UUID.randomUUID().toString();
        this.evento = evento;           
        this.localidad = localidad;
        this.precioBase = precioBase;
        this.porcentajeServicio = porcentajeServicio;
        this.cuotaFija = cuotaFija;
        this.estado = TicketEstado.DISPONIBLE;
    }

    
	

	public String getId() { return id; }
    public Evento getEvento() { return evento; }
    public Localidad getLocalidad() { return localidad; }
    public double getPrecioBase() { return precioBase; }
    public double getPorcentajeServicio() { return porcentajeServicio; }
    public double getCuotaFija() { return cuotaFija; }
    public TicketEstado getEstado() { return estado; }
    public Usuario getPropietario() { return propietario; }
    public double getPrecioFinal() { 
        return precioFinal(); 
    }
    public void setEvento(Evento evento) {
		this.evento = evento;
	}


    public void setEstado(TicketEstado nuevoEstado) {
        if (nuevoEstado == null) throw new IllegalArgumentException("Estado no puede ser null");
        this.estado = nuevoEstado;
    }

    public double precioFinal() {
        return precioBase + precioBase * porcentajeServicio + cuotaFija;
    }

    public void venderA(Usuario u) {
        if (ticketVencido()) throw new IllegalStateException("Ticket vencido, no se puede vender");
        if (estado != TicketEstado.DISPONIBLE) throw new IllegalStateException("Ticket no disponible para venta");
        if (u == null) throw new IllegalArgumentException("Usuario inválido");
        this.propietario = u;
        this.estado = TicketEstado.VENDIDO;
    }

    public abstract boolean esTransferible();

    public void transferirA(Usuario nuevoPropietario, Usuario actual, String passwordDelActual) {
        if (!esTransferible()) throw new IllegalStateException("Ticket no transferible");
        if (propietario == null) throw new IllegalStateException("Ticket sin propietario");
        if (!propietario.getLogin().equals(actual.getLogin())) throw new IllegalStateException("Solo propietario puede transferir");
        if (!actual.checkPassword(passwordDelActual)) throw new IllegalArgumentException("Contraseña incorrecta");
        if (ticketVencido()) throw new IllegalStateException("Ticket vencido no se puede transferir");
        this.propietario = nuevoPropietario;
        this.estado = TicketEstado.TRANSFERIDO;
    }
    public double precioConDescuento(Oferta oferta) {
        if (oferta != null && oferta.estaVigente() && 
            oferta.getLocalidad().equals(this.localidad)) {
            return oferta.aplicarDescuento(precioFinal());
        }
        return precioFinal();
    }
    
    public boolean isImpreso() {
        return impreso;
    }

    public void marcarComoImpreso() {
        if (this.impreso) {
            throw new IllegalStateException("El ticket ya ha sido marcado como impreso.");
        }
        this.impreso = true;
        
    }

    
    public boolean ticketVencido() {
        if (evento == null) return false;
        LocalDateTime ahora = LocalDateTime.now();
        return evento.getFechaHora().isBefore(ahora);
    }

    @Override
    public String toString() {
        return "Ticket{" + id + ", precioBase=" + precioBase + ", estado=" + estado + ", propietario=" + (propietario!=null?propietario.getLogin():"-") + "}";
    }




}
