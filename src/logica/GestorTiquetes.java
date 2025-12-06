package logica;
import java.util.ArrayList;
import java.util.List;

import boletamaster.app.Sistema;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.tiquetes.Ticket;
import boletamaster.tiquetes.TicketDeluxe;
import boletamaster.tiquetes.TicketMultiple;
import boletamaster.tiquetes.TicketNumerado;
import boletamaster.tiquetes.TicketSimple;
import boletamaster.usuarios.Comprador;

public class GestorTiquetes {

    private final Sistema sistema;

    public GestorTiquetes(Sistema sistema) {
        if (sistema == null) throw new IllegalArgumentException("Sistema requerido");
        this.sistema = sistema;
    }
    
    private double getPorcentajeServicio() {
        return sistema.getCore().getGestorFinanzas().getPorcentajeServicioGlobal();
    }
    private double getCuotaFija() {
        return sistema.getCore().getGestorFinanzas().getCuotaFijaGlobal();
    }

    public TicketSimple crearTicketSimple(Localidad localidad) {
        
        Evento evento = null; 
        double precio = localidad.getPrecioBase(); 

        double porcentaje = getPorcentajeServicio();
        double cuota = getCuotaFija();
        
        TicketSimple nuevoTicket = new TicketSimple(evento, localidad, precio, porcentaje, cuota);
        sistema.getRepo().addTicket(nuevoTicket); 
        return nuevoTicket;
    }

    public TicketNumerado crearTicketNumerado(Localidad localidad, String asiento) {
        Evento evento = null;
        double precio = localidad.getPrecioBase();
        double porcentaje = getPorcentajeServicio();
        double cuota = getCuotaFija();
        
        TicketNumerado nuevoTicket = new TicketNumerado(evento, localidad, precio, porcentaje, cuota, asiento);
        sistema.getRepo().addTicket(nuevoTicket);
        return nuevoTicket;
    }

    public TicketDeluxe crearTicketDeluxe(Localidad localidad) {
        Evento evento = null;
        double precio = localidad.getPrecioBase();
        double porcentaje = getPorcentajeServicio();
        double cuota = getCuotaFija();
        
        TicketDeluxe nuevoTicket = new TicketDeluxe(evento, localidad, precio, porcentaje, cuota);
        sistema.getRepo().addTicket(nuevoTicket);
        return nuevoTicket;
    }

    public TicketMultiple crearTicketMultiple(Localidad localidad, int cantidad, double precioPaquete) {
        Evento evento = null;
        double porcentaje = getPorcentajeServicio();
        double cuota = getCuotaFija();
        
        TicketMultiple tm = new TicketMultiple(evento, localidad, precioPaquete, porcentaje, cuota);
        
        for (int i = 0; i < cantidad; i++) {
            tm.addElemento(crearTicketSimple(localidad)); 
        }
        sistema.getRepo().addTicket(tm);
        return tm;
    }
    
    public void venderTicket(Ticket ticket, Comprador comprador, String infoPago, String password) {
        if (ticket == null || comprador == null) {
            throw new IllegalArgumentException("Ticket o Comprador inválido para la venta.");
        }
        
        
        try {
            
            ticket.venderA(comprador);
            
        } catch (IllegalStateException | IllegalArgumentException e) {
            System.err.println("Error al vender ticket durante la carga de datos: " + e.getMessage());
        }
    }

    public List<Ticket> generarTicketsParaLocalidad(Evento evento, Localidad localidad, int cantidad) {
        List<Ticket> lista = new ArrayList<>();
        if (localidad.isNumerada()) {
            List<String> asientos = localidad.getAsientosNumerados();
            int limite = Math.min(cantidad, asientos.size());
            for (int i = 0; i < limite; i++) {
                lista.add(crearTicketNumerado(localidad, asientos.get(i)));
            }
        } else {
            for (int i = 0; i < cantidad; i++) {
                lista.add(crearTicketSimple(localidad));
            }
        }
        return lista;
    }
}
