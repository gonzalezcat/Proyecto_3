package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.marketplace.Marketplace;
import boletamaster.marketplace.LogRegistro;
import boletamaster.usuarios.Administrador;
import boletamaster.usuarios.Usuario;
import logica.BoletamasterSystem;
import boletamaster.eventos.Evento;
import boletamaster.transacciones.Reembolso;
import java.util.List;

public class MainAdministrador {

    public static void main(String[] args) {
        BoletamasterSystem core = BoletamasterSystem.getInstance();
        Sistema sistema = new Sistema(core);

        while (true) {
            System.out.println("\n=== BIENVENIDO A BOLETAMASTER (ADMIN) ===");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrar Nuevo Administrador");
            System.out.println("3. Salir");
            
            int opcion = ConsoleUtils.readInt("Seleccione una opción", 1, 3);
            if (opcion == 3) return;

            Administrador adminActual = null;

            if (opcion == 1) {
                String login = ConsoleUtils.readLine("Usuario");
                String pass = ConsoleUtils.readPassword("Contraseña");
                Usuario u = sistema.buscarUsuarioPorLogin(login);
                if (u != null && u.checkPassword(pass) && u instanceof Administrador) {
                    adminActual = (Administrador) u;
                } else {
                    System.out.println("Acceso denegado.");
                }
            } else if (opcion == 2) {
                System.out.println("--- Crear Admin ---");
                String login = ConsoleUtils.readLine("Usuario");
                String pass = ConsoleUtils.readPassword("Contraseña");
                String nombre = ConsoleUtils.readLine("Nombre");
                adminActual = new Administrador(login, pass, nombre);
                sistema.registrarUsuario(adminActual);
                System.out.println("Admin creado.");
            }

            if (adminActual != null) {
                menuAdmin(sistema, adminActual);
            }
        }
    }

    private static void menuAdmin(Sistema sistema, Administrador admin) {
        Marketplace marketplace = BoletamasterSystem.getInstance().getMarketplace();

        while (true) {
            System.out.println("\n--- MENÚ ADMIN (" + admin.getNombre() + ") ---");
            System.out.println("1. Cancelar evento y reembolsar");
            System.out.println("2. Ver Log del Marketplace");
            System.out.println("3. Eliminar Oferta Marketplace");
            System.out.println("4. Cerrar Sesión");

            int opt = ConsoleUtils.readInt("Opción", 1, 4);
            try {
                switch (opt) {
                    case 1:
                        
                        System.out.println("Funcionalidad pendiente de implementación UI completa.");
                        break;
                    case 2:
                        List<LogRegistro> logs = sistema.getRepo().getLog();
                        if (logs.isEmpty()) System.out.println("Log vacío.");
                        for(LogRegistro l : logs) System.out.println(l);
                        break;
                    case 3:
                        String id = ConsoleUtils.readLine("ID Oferta a eliminar");
                        try {
                            marketplace.borrarOfertaPorAdmin(id, admin);
                            System.out.println("Oferta eliminada.");
                        } catch (Exception e) { System.out.println(e.getMessage()); }
                        break;
                    case 4: return;
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}