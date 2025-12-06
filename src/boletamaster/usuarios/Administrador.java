package boletamaster.usuarios;

import java.util.List;

import boletamaster.app.Sistema;
import boletamaster.tiquetes.Ticket;
import boletamaster.tiquetes.TicketMultiple;
import boletamaster.transacciones.Reembolso;

public class Administrador extends Usuario {
	private static final long serialVersionUID = 1L;


    public Administrador(String login, String password, String nombre) {
        super(login, password, nombre);
    }

    public Reembolso reembolso(Ticket t, Sistema sistema) {
        if (t == null) throw new IllegalArgumentException("Ticket nulo");
        if (sistema == null) throw new IllegalArgumentException("Sistema nulo");
        if (t.getPropietario() == null)
            throw new IllegalStateException("Ticket no tiene propietario para reembolsar");

        double montoReembolso = t.getPrecioBase() + (t.getPrecioBase() * t.getPorcentajeServicio());
        t.getPropietario().depositarSaldo(montoReembolso);

        Reembolso r = new Reembolso(t.getPropietario(), montoReembolso);
        sistema.getRepo().addTransaccion(r);

        t.setEstado(boletamaster.tiquetes.TicketEstado.CANCELADO);
        return r;
    }

    public double[] gananciasGenerales(Sistema sistema) {
        if (sistema == null) throw new IllegalArgumentException("Sistema nulo");

        double ventasTotales = 0.0;
        double gananciasPlataforma = 0.0;

        List<Ticket> tickets = sistema.getRepo().getTickets();

        for (Ticket t : tickets) {
            if (t.getEstado() == boletamaster.tiquetes.TicketEstado.VENDIDO
                || t.getEstado() == boletamaster.tiquetes.TicketEstado.TRANSFERIDO) {

                if (t instanceof TicketMultiple) {
                    ventasTotales += t.getPrecioBase();
                    gananciasPlataforma += t.getPrecioBase() * t.getPorcentajeServicio() + t.getCuotaFija();
                } else {
                    ventasTotales += t.getPrecioBase();
                    gananciasPlataforma += t.getPrecioBase() * t.getPorcentajeServicio() + t.getCuotaFija();
                }
            }
        }
        return new double[]{ventasTotales, gananciasPlataforma};
    }

    @Override
    public String toString() {
        return "Administrador{" + "login=" + login + ", nombre=" + nombre + '}';
    }
}
