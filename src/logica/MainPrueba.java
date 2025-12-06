package logica;

import java.time.LocalDateTime;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Venue;
import boletamaster.usuarios.Organizador;
import boletamaster.app.Sistema; 

public class MainPrueba {
    public static void main(String[] args) {
        BoletamasterSystem core = BoletamasterSystem.getInstance();
        Sistema fachada = new Sistema(core); 

        try {
            
            Organizador organizador = new Organizador("org1", "pass456", "Eventos SA");
            Venue venue = new Venue("V001", "Estadio Nacional", "Ciudad", 50000, true);


            
            String nombreEvento = "Concierto Rock";
            LocalDateTime fechaEvento = LocalDateTime.now().plusDays(30);

            
            Evento nuevoEvento = fachada.registrarEvento(
                organizador,
                nombreEvento,
                fechaEvento,
                venue
            );
            
            System.out.println("✅ Evento creado: " + nuevoEvento.getNombre());
            System.out.println("Organizador: " + organizador.getNombre());
            System.out.println("Venue: " + venue.getNombre());
            
            System.out.println("Eventos totales: " + fachada.getCore().getGestorEventos().getEventos().size());

            System.out.println("\n--- Estado del sistema ---");
            System.out.println(core); 
        } catch (Exception e) {
            System.err.println("❌ Error en prueba: " + e.getMessage());
            e.printStackTrace(); 
        }
    }
}
