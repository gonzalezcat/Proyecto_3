package logica;

import boletamaster.app.Sistema;
import boletamaster.usuarios.Usuario;
import boletamaster.transacciones.Reembolso;
import boletamaster.tiquetes.*;

public class GestorReembolsos {
    
    private final Sistema sistema; 

    public GestorReembolsos(Sistema sistema) {
        this.sistema = sistema;
    }
    
    public Reembolso procesarReembolsoCompleto(Usuario usuario, double monto, String motivo) {
        // ...
        usuario.depositarSaldo(monto);
        return new Reembolso(usuario, monto);
    }
    
    
    public Reembolso procesarReembolsoParcial(Usuario usuario, double montoBase, String motivo) {
        usuario.depositarSaldo(montoBase);
        return new Reembolso(usuario, montoBase);
    }
    
    public boolean puedeReembolsar(Ticket ticket) {
        return ticket.getEstado() != TicketEstado.USADO && 
               ticket.getEstado() != TicketEstado.CANCELADO;
    }
}