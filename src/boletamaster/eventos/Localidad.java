package boletamaster.eventos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.io.Serializable;

public class Localidad implements Serializable{
    private final String id;
    private final String nombre;
    private final double precioBase;
    private final int capacidad;
    private final boolean numerada;
    private final List<String> asientosNumerados; 
    private static final long serialVersionUID = 1L;

    public Localidad(String id, String nombre, double precioBase, int capacidad, boolean numerada) {
        this.id = id;
        this.nombre = nombre;
        this.precioBase = precioBase;
        this.capacidad = capacidad;
        this.numerada = numerada;
        this.asientosNumerados = numerada ? new ArrayList<>() : null;
    }

    
    public String getId() { return id; }
    public String getNombre() { return nombre; }
    public double getPrecioBase() { return precioBase; }
    public int getCapacidad() { return capacidad; }
    public boolean isNumerada() { return numerada; }
    public List<String> getAsientosNumerados() { return asientosNumerados; }

    public void addAsiento(String etiqueta) {
        if (!numerada) throw new IllegalStateException("Localidad no numerada");
        this.asientosNumerados.add(etiqueta);
    }

    @Override
    public String toString() {
        return "Localidad{" + nombre + ", precioBase=" + precioBase + ", capacidad=" + capacidad + ", numerada=" + numerada + '}';
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Localidad localidad = (Localidad) o;
        return Objects.equals(id, localidad.id);
    }
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
}
