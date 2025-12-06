package boletamaster.transacciones;

import boletamaster.usuarios.Usuario;

public class Reembolso extends Transaccion {
	private static final long serialVersionUID = 1L;

    public Reembolso(Usuario usuario, double montoTotal) {
        super(usuario, montoTotal);
    }

    @Override
    public String toString() {
        return "Reembolso{" + getId() + ", usuario=" + usuario.getLogin() + ", monto=" + montoTotal + "}";
    }
}

