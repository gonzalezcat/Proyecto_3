package logica;

import boletamaster.app.Sistema;

public class GestorFinanzas {
    private final Sistema sistema;

    private double cuotaFijaGlobal = 0.0;
    private double porcentajeServicioGlobal = 0.0;

    public GestorFinanzas(Sistema sistema) {
        if (sistema == null) throw new IllegalArgumentException("Sistema requerido");
        this.sistema = sistema;
    }

    public void agregarTransaccion(Object transaccion) {
        sistema.getRepo().addTransaccion(transaccion);
    }

    public void setCuotaFijaGlobal(double cuotaFijaGlobal) {
        if (cuotaFijaGlobal < 0) throw new IllegalArgumentException("Cuota fija inválida");
        this.cuotaFijaGlobal = cuotaFijaGlobal;
    }

    public double getCuotaFijaGlobal() {
        return cuotaFijaGlobal;
    }

    public void setPorcentajeServicioGlobal(double porcentajeServicioGlobal) {
        if (porcentajeServicioGlobal < 0 || porcentajeServicioGlobal > 1)
            throw new IllegalArgumentException("Porcentaje de servicio inválido");
        this.porcentajeServicioGlobal = porcentajeServicioGlobal;
    }

    public double getPorcentajeServicioGlobal() {
        return porcentajeServicioGlobal;
    }
}
