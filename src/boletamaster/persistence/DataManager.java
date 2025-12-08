package boletamaster.persistence;

import java.io.*;
import java.util.*;

public class DataManager {

    private static void crearDirectorioDeDatos() {
        File dir = new File(PersistenceConfig.DATA_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public static <T> void guardarLista(String ruta, List<T> lista) {
        crearDirectorioDeDatos(); 

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(ruta))) {
            oos.writeObject(lista);
            
        } catch (NotSerializableException e) {
             System.err.println("ERROR FATAL: El objeto " + e.getMessage() + " no implementa Serializable. No se guardó la lista en " + ruta);
        } catch (IOException e) {
            System.err.println("Error guardando " + ruta + ": " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> cargarLista(String ruta) {
        File f = new File(ruta);
        if (!f.exists()) return new ArrayList<>(); 
        
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(ruta))) {
            Object obj = ois.readObject();
            if (obj instanceof List) {
                 return (List<T>) obj;
            } else {
                 System.err.println("Advertencia: El contenido del archivo " + ruta + " no es una lista esperada.");
                 return new ArrayList<>();
            }
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (EOFException e) {
            System.err.println("El archivo " + ruta + " está vacío o dañado. Se reinicia la lista.");
            return new ArrayList<>();
        } catch (InvalidClassException e) {
             System.err.println("Error de versión de clase en " + ruta + ". Datos incompatibles: " + e.getMessage());
             return new ArrayList<>();
        } catch (Exception e) {
            System.err.println("Error general cargando " + ruta + ": " + e.getMessage());
            return new ArrayList<>();
        }
    }
}