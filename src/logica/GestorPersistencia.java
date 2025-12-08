package logica;

import boletamaster.app.Sistema;
import boletamaster.persistence.SimpleRepository;
import java.io.*;
import java.util.List;
import java.util.ArrayList; // Necesario para evitar errores de cast
import boletamaster.eventos.*;
import boletamaster.tiquetes.*;
import boletamaster.transacciones.*;
import boletamaster.usuarios.*;
import boletamaster.marketplace.*;
import boletamaster.marketplace.Oferta; 

public class GestorPersistencia {
    
    private final Sistema sistema;
    private static final String CARPETA_DATOS = "data/";
    
    private static final String ARCHIVO_USUARIOS = "usuarios.dat";
    private static final String ARCHIVO_EVENTOS = "eventos.dat";
    private static final String ARCHIVO_VENUES = "venues.dat";
    private static final String ARCHIVO_TICKETS = "tickets.dat";
    private static final String ARCHIVO_TRANSACCIONES = "transacciones.dat";
    private static final String ARCHIVO_OFERTAS = "ofertas.dat"; 
    private static final String ARCHIVO_LOG = "log.dat";         
    
    public GestorPersistencia(Sistema sistema) {
        this.sistema = sistema;
    }
    
    
    public void cargarTodo() {
        System.out.println("Cargando datos persistentes...");
        SimpleRepository repo = sistema.getRepo();
        
        List<Usuario> usuarios = cargarObjeto(ARCHIVO_USUARIOS);
        if (usuarios != null) repo.setUsuarios(usuarios); 
        
        List<Venue> venues = cargarObjeto(ARCHIVO_VENUES);
        if (venues != null) repo.setVenues(venues); 
        
        List<Evento> eventos = cargarObjeto(ARCHIVO_EVENTOS);
        if (eventos != null) repo.setEventos(eventos);
        
        List<Ticket> tickets = cargarObjeto(ARCHIVO_TICKETS);
        if (tickets != null) repo.setTickets(tickets);
        
        List<Object> transacciones = cargarObjeto(ARCHIVO_TRANSACCIONES);
        if (transacciones != null) repo.setTransacciones(transacciones);

        List<Oferta> ofertas = cargarObjeto(ARCHIVO_OFERTAS);
        if (ofertas != null) repo.setOfertas(ofertas);

        List<LogRegistro> log = cargarObjeto(ARCHIVO_LOG);
        if (log != null) repo.setLog(log);
        
        System.out.println("Carga de datos persistentes finalizada.");
    }
    
    
    public void guardarUsuarios(List<Usuario> usuarios) {
        guardarObjeto(usuarios, ARCHIVO_USUARIOS);
    }
    
    public void guardarEventos(List<Evento> eventos) {
        guardarObjeto(eventos, ARCHIVO_EVENTOS);
    }
    
    public void guardarVenues(List<Venue> venues) {
        guardarObjeto(venues, ARCHIVO_VENUES);
    }
    
    public void guardarTickets(List<Ticket> tickets) { 
        guardarObjeto(tickets, ARCHIVO_TICKETS);
    }
    
    public void guardarTransacciones(List<Object> transacciones) { 
        guardarObjeto(transacciones, ARCHIVO_TRANSACCIONES);
    }
    
    public void guardarOfertas(List<Oferta> ofertas) {
        guardarObjeto(ofertas, ARCHIVO_OFERTAS);
    }
    
    public void guardarLog(List<LogRegistro> log) { 
        guardarObjeto(log, ARCHIVO_LOG);
    }
    
    
    
    @SuppressWarnings("unchecked")
    public <T> T cargarObjeto(String archivo) {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(CARPETA_DATOS + archivo))) {
            return (T) ois.readObject();
        } catch (FileNotFoundException e) {
            return null; 
        } catch (Exception e) {
            System.err.println("Error cargando datos desde " + archivo + ": " + e.getMessage());
            e.printStackTrace();
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