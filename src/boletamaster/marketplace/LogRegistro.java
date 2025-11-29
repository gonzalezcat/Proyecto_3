package boletamaster.marketplace;

import java.time.LocalDateTime;
import java.io.Serializable;

public class LogRegistro implements Serializable{
    private final LocalDateTime fechaHora;
    private final String tipoAccion; 
    private final String descripcion;

    private static final long serialVersionUID = 1L;
    
    public LogRegistro(String tipoAccion, String descripcion) {
        this.fechaHora = LocalDateTime.now();
        this.tipoAccion = tipoAccion;
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHora() { return fechaHora; }
    public String getTipoAccion() { return tipoAccion; } 
    public String getDescripcion() { return descripcion; }

    @Override
    public String toString() {
        return "[" + fechaHora + "] - " + tipoAccion + ": " + descripcion;
    }
}