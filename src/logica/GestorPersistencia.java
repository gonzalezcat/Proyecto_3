package logica;

import boletamaster.app.Sistema; 
import java.io.*;
import java.util.List;
import boletamaster.eventos.*;
import boletamaster.transacciones.*;
import boletamaster.usuarios.*;

public class GestorPersistencia {
    
    private final Sistema sistema; 
    private static final String CARPETA_DATOS = "data/";
    
    public GestorPersistencia(Sistema sistema) {
        this.sistema = sistema;
    }
    
    
    public void guardarUsuarios(List<Usuario> usuarios) {
        guardarObjeto(usuarios, "usuarios.dat");
    }
    
    public void guardarEventos(List<Evento> eventos) {
        guardarObjeto(eventos, "eventos.dat");
    }
    
    public void guardarVenues(List<Venue> venues) {
        guardarObjeto(venues, "venues.dat");
    }
    
    public void guardarTransacciones(List<Transaccion> transacciones) {
        guardarObjeto(transacciones, "transacciones.dat");
    }
    
    @SuppressWarnings("unchecked")
    public <T> T cargarDatos(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(
             new FileInputStream(CARPETA_DATOS + archivo))) {
            return (T) ois.readObject();
        } catch (Exception e) {
            System.err.println("Error cargando datos desde " + archivo + ": " + e.getMessage()); 
            return null; 
        }
    }
    
    private void guardarObjeto(Object objeto, String archivo) {
        crearCarpetaSiNoExiste();
        try (ObjectOutputStream oos = new ObjectOutputStream(
             new FileOutputStream(CARPETA_DATOS + archivo))) {
            oos.writeObject(objeto);
        } catch (IOException e) {
            System.err.println("Error guardando datos en " + archivo + ": " + e.getMessage());
        }
    }
    
    private void crearCarpetaSiNoExiste() {
        File carpeta = new File(CARPETA_DATOS);
        if (!carpeta.exists()) {
            carpeta.mkdirs();
        }
    }
}