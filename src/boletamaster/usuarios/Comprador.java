package boletamaster.usuarios;

public class Comprador extends Usuario {
	private static final long serialVersionUID = 1L;

    public Comprador(String login, String password, String nombre) {
        super(login, password, nombre);
    }

    @Override
    public String toString() {
        return "Comprador{" + "login=" + login + ", nombre=" + nombre + ", saldo=" + saldo + '}';
    }
}
