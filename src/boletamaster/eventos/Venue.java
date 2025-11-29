package boletamaster.eventos;
import java.io.Serializable;

public class Venue implements Serializable {
    private final String id;
    private final String nombre;
    private final String ubicacion;
    private final int capacidadMaxima;
    private boolean aprobado;
    private static final long serialVersionUID = 1L;

    public Venue(String id, String nombre, String ubicacion, int capacidadMaxima, boolean aprobado) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.capacidadMaxima = capacidadMaxima;
        this.aprobado = aprobado;
    }

    //metodos
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public String getUbicacion() { return ubicacion; }
    public int getCapacidadMaxima() { return capacidadMaxima; }
    public boolean isAprobado() { return aprobado; }
    public void setAprobado(boolean aprobado) { this.aprobado = aprobado; }

    @Override
    public String toString() {
        return "Venue{" + nombre + " @ " + ubicacion + ", cap=" + capacidadMaxima + ", aprobado=" + aprobado + "}";
    }
}

