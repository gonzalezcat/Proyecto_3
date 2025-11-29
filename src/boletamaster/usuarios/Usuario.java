package boletamaster.usuarios;
import java.io.Serializable;
import java.util.UUID;

public abstract class Usuario implements Serializable {
    protected final String id;
    protected final String login;
    protected String password;
    protected String nombre;
    protected double saldo; // reembolsos
    private static final long serialVersionUID = 1L;

    public Usuario(String login, String password, String nombre) {
        this.id = UUID.randomUUID().toString();
        this.login = login;
        this.password = password;
        this.nombre = nombre;
        this.saldo = 0.0;
    }
    
    //metodos

    public String getId() { return id; }
    public String getLogin() { return login; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getSaldo() { return saldo; }
    public void depositarSaldo(double monto) {
        if (monto < 0) throw new IllegalArgumentException("Monto negativo");
        this.saldo += monto;
    }
    public void descontarSaldo(double monto) {
        if (monto < 0) throw new IllegalArgumentException("Monto negativo");
        if (monto > saldo) throw new IllegalStateException("Saldo insuficiente");
        this.saldo -= monto;
    }
    public boolean checkPassword(String pass) {
        return this.password.equals(pass);
    }
}
