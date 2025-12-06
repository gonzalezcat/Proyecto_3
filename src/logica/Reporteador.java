package logica;

import boletamaster.app.Sistema;
import boletamaster.transacciones.Compra;
import boletamaster.usuarios.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class Reporteador {

    private final GestorFinanzas gestorFinanzas;
    private final Sistema sistema;

    public Reporteador(Sistema sistema, GestorFinanzas gestorFinanzas) {
        if (sistema == null) throw new IllegalArgumentException("Sistema requerido");
        if (gestorFinanzas == null) throw new IllegalArgumentException("GestorFinanzas requerido");
        this.sistema = sistema;
        this.gestorFinanzas = gestorFinanzas;
    }

    public Map<String, Object> generarReportePorOrganizador(Organizador org) {
        Map<String, Object> reporte = new HashMap<>();
        double total = 0.0;
        int vendidos = 0;

        for (Object o : sistema.getRepo().getTransacciones()) {
            if (o instanceof Compra c && c.getUsuario() instanceof Comprador) {
                vendidos++;
                total += c.getMontoTotal();
            }
        }

        reporte.put("tipo", "ORGANIZADOR");
        reporte.put("organizador", org.getNombre());
        reporte.put("ticketsVendidos", vendidos);
        reporte.put("ganancias", total);
        reporte.put("cuotaFijaGlobal", gestorFinanzas.getCuotaFijaGlobal());
        reporte.put("porcentajeServicioGlobal", gestorFinanzas.getPorcentajeServicioGlobal());
        return reporte;
    }

    public Map<String, Object> generarReporteAdministrador(LocalDateTime inicio, LocalDateTime fin) {
        Map<String, Object> reporte = new HashMap<>();
        double total = 0.0;

        for (Object o : sistema.getRepo().getTransacciones()) {
            if (o instanceof Compra c) {
                total += c.getMontoTotal();
            }
        }

        reporte.put("tipo", "ADMINISTRADOR");
        reporte.put("periodo", inicio + " a " + fin);
        reporte.put("ingresosTotales", total);
        reporte.put("cuotaFijaGlobal", gestorFinanzas.getCuotaFijaGlobal());
        reporte.put("porcentajeServicioGlobal", gestorFinanzas.getPorcentajeServicioGlobal());
        return reporte;
    }

    public String generarReporteTexto(Map<String, Object> reporte) {
        StringBuilder sb = new StringBuilder("=== REPORTE ===\n");
        for (Map.Entry<String, Object> e : reporte.entrySet()) {
            sb.append(e.getKey()).append(": ").append(e.getValue()).append("\n");
        }
        return sb.toString();
    }
}
