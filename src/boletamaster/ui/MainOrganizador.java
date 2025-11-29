package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.eventos.Evento;
import boletamaster.eventos.Localidad;
import boletamaster.eventos.Venue;
import boletamaster.tiquetes.Ticket;
import boletamaster.usuarios.Organizador;
import boletamaster.usuarios.Usuario;
import logica.BoletamasterSystem;

import java.time.LocalDateTime;
import java.util.List;

public class MainOrganizador {
    public static void main(String[] args) {
        BoletamasterSystem core = BoletamasterSystem.getInstance();
        Sistema sistema = new Sistema(core);

        while (true) {
            System.out.println("\n=== BIENVENIDO A BOLETAMASTER (ORGANIZADORES) ===");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
            
            int opcion = ConsoleUtils.readInt("Seleccione una opción", 1, 3);

            if (opcion == 3) return;

            Organizador orgActual = null;

            if (opcion == 1) {
                String login = ConsoleUtils.readLine("Usuario");
                String pass = ConsoleUtils.readPassword("Contraseña");
                Usuario u = sistema.buscarUsuarioPorLogin(login);
                if (u != null && u.checkPassword(pass) && u instanceof Organizador) {
                    orgActual = (Organizador) u;
                } else {
                    System.out.println("Credenciales inválidas.");
                }
            } else if (opcion == 2) {
                System.out.println("\n--- Registro de Organizador ---");
                String login = ConsoleUtils.readLine("Nuevo Usuario");
                if (sistema.buscarUsuarioPorLogin(login) != null) {
                    System.out.println("Usuario ya existe.");
                    continue;
                }
                String pass = ConsoleUtils.readPassword("Contraseña");
                String nombre = ConsoleUtils.readLine("Nombre Organización");
                
                orgActual = new Organizador(login, pass, nombre);
                sistema.registrarUsuario(orgActual);
                System.out.println("Registrado exitosamente.");
            }

            if (orgActual != null) {
                menuPrincipal(sistema, orgActual);
            }
        }
    }

    private static void menuPrincipal(Sistema sistema, Organizador org) {
        while (true) {
            System.out.println("\n--- MENÚ ORGANIZADOR (" + org.getNombre() + ") ---");
            System.out.println("1. Ver eventos activos");
            System.out.println("2. Crear evento");
            System.out.println("3. Crear localidad en un evento");
            System.out.println("4. Crear Venue (Lugar)"); // Agregado para poder crear eventos
            System.out.println("5. Salir");
            
            int opt = ConsoleUtils.readInt("Elija opción", 1, 5);
            try {
                switch (opt) {
                    case 1: verEventosActivos(sistema, org); break;
                    case 2: crearEvento(sistema, org); break;
                    case 3: crearLocalidad(sistema, org); break;
                    case 4: crearVenue(sistema); break;
                    case 5: return;
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    // --- Métodos auxiliares ---
    
    private static void crearVenue(Sistema sistema) {
        System.out.println("--- Crear Nuevo Venue ---");
        String id = ConsoleUtils.readLine("ID del Venue");
        String nombre = ConsoleUtils.readLine("Nombre");
        String ciudad = ConsoleUtils.readLine("Ciudad");
        int capacidad = ConsoleUtils.readInt("Capacidad Total", 100, 100000);
        // Creamos el venue aprobado por defecto para facilitar pruebas
        Venue v = new Venue(id, nombre, ciudad, capacidad, true); 
        sistema.registrarVenue(v);
        System.out.println("Venue creado exitosamente.");
    }

    private static void verEventosActivos(Sistema sistema, Organizador org) {
        List<Evento> evs = sistema.eventosActivosPorOrganizador(org);
        if (evs.isEmpty()) System.out.println("No hay eventos activos.");
        for (Evento e : evs) System.out.println("ID: " + e.getId() + " | " + e.getNombre());
    }

    private static void crearEvento(Sistema sistema, Organizador org) {
        String id = ConsoleUtils.readLine("ID Evento");
        String nombre = ConsoleUtils.readLine("Nombre Evento");
        String idVenue = ConsoleUtils.readLine("ID del Venue existente");
        
        // Buscar venue manualmente en la lista del sistema
        Venue v = null;
        for(Venue ven : BoletamasterSystem.getInstance().getVenues()) {
             if(ven.getId().equals(idVenue)) v = ven;
        }

        if (v == null) {
            System.out.println("Venue no encontrado. Cree uno primero (Opción 4).");
            return;
        }

        String fechaStr = ConsoleUtils.readLine("Fecha (YYYY-MM-DDTHH:MM) ej: 2025-10-20T20:00");
        try {
            LocalDateTime fecha = LocalDateTime.parse(fechaStr);
            Evento e = new Evento(id, nombre, fecha, v, org);
            sistema.registrarEvento(e);
            System.out.println("Evento creado.");
        } catch (Exception e) {
            System.out.println("Formato de fecha incorrecto.");
        }
    }

    private static void crearLocalidad(Sistema sistema, Organizador org) {
        String idEvt = ConsoleUtils.readLine("ID Evento");
        // Buscar evento simple
        Evento e = null;
        for(Evento evt : sistema.eventosActivosPorOrganizador(org)) {
            if(evt.getId().equals(idEvt)) e = evt;
        }
        
        if (e == null) { System.out.println("Evento no encontrado o no es tuyo."); return; }

        String nombreLoc = ConsoleUtils.readLine("Nombre Localidad");
        double precio = ConsoleUtils.readDouble("Precio", 1, 1000000);
        int cap = ConsoleUtils.readInt("Capacidad", 1, 10000);
        
        Localidad loc = new Localidad("LOC-"+System.currentTimeMillis(), nombreLoc, precio, cap, false);
        e.addLocalidad(loc);
        
        // Generar tickets automáticamente para pruebas
        System.out.println("Generando tickets para la localidad...");
        for(int i=0; i<cap; i++) {
            // Usamos el gestor de tiquetes del core
            Ticket t = sistema.generarTicketSimple(loc);
            e.addTicket(t);
            BoletamasterSystem.getInstance().getRepo().addTicket(t); // Guardar en repo
        }
        System.out.println("Localidad y " + cap + " tickets creados.");
    }
}