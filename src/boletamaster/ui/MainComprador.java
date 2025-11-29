package boletamaster.ui;

import boletamaster.app.Sistema;
import boletamaster.marketplace.Marketplace;
import boletamaster.ui.MarketplaceUI;
import boletamaster.tiquetes.Ticket;
import boletamaster.transacciones.Compra;
import boletamaster.usuarios.Comprador;
import boletamaster.usuarios.Usuario;
import logica.BoletamasterSystem;

public class MainComprador {

    public static void main(String[] args) {
        // 1. Inicializar el sistema correctamente
        BoletamasterSystem core = BoletamasterSystem.getInstance();
        Sistema sistema = new Sistema(core);

        while (true) {
            System.out.println("\n=== BIENVENIDO A BOLETAMASTER (COMPRADORES) ===");
            System.out.println("1. Iniciar Sesión");
            System.out.println("2. Registrarse");
            System.out.println("3. Salir");
            
            int opcion = ConsoleUtils.readInt("Seleccione una opción", 1, 3);

            if (opcion == 3) {
                System.out.println("Adiós.");
                return;
            }

            Comprador compradorActual = null;

            if (opcion == 1) {
                // --- LOGIN ---
                String login = ConsoleUtils.readLine("Usuario");
                String pass = ConsoleUtils.readPassword("Contraseña");
                Usuario u = sistema.buscarUsuarioPorLogin(login);
                
                if (u != null && u.checkPassword(pass) && u instanceof Comprador) {
                    compradorActual = (Comprador) u;
                } else {
                    System.out.println("Error: Credenciales inválidas o el usuario no existe.");
                }

            } else if (opcion == 2) {
                // --- REGISTRO ---
                System.out.println("\n--- Registro de Nuevo Comprador ---");
                String nuevoLogin = ConsoleUtils.readLine("Ingrese nuevo Usuario");
                
                if (sistema.buscarUsuarioPorLogin(nuevoLogin) != null) {
                    System.out.println("Error: Ese usuario ya existe.");
                    continue;
                }
                
                String nuevaPass = ConsoleUtils.readPassword("Ingrese Contraseña");
                String nombre = ConsoleUtils.readLine("Nombre Completo");
                
                compradorActual = new Comprador(nuevoLogin, nuevaPass, nombre);
                sistema.registrarUsuario(compradorActual);
                System.out.println("¡Registro exitoso! Sesión iniciada automáticamente.");
            }

            
            if (compradorActual != null) {
                mostrarMenuPrincipal(sistema, compradorActual);
            }
        }
    }

    private static void mostrarMenuPrincipal(Sistema sistema, Comprador comprador) {
        
        MarketplaceUI marketplaceUI = new MarketplaceUI(
            BoletamasterSystem.getInstance().getMarketplace(), 
            sistema, 
            comprador
        );

        while (true) {
            System.out.println("\n--- MENÚ COMPRADOR (" + comprador.getNombre() + ") ---");
            System.out.println("1. Ver saldo");
            System.out.println("2. Recargar Saldo (Simulado)");
            System.out.println("3. Ver mis tickets");
            System.out.println("4. Ir al Marketplace (Reventa)");
            System.out.println("5. Cerrar Sesión");

            int opt = ConsoleUtils.readInt("Elija opción", 1, 5);
            try {
                switch (opt) {
                    case 1:
                        System.out.println("Saldo actual: " + comprador.getSaldo());
                        break;
                    case 2:
                        double monto = ConsoleUtils.readDouble("Monto a recargar", 1, 1000000);
                        comprador.depositarSaldo(monto);
                        System.out.println("Recarga exitosa. Nuevo saldo: " + comprador.getSaldo());
                        break;
                    case 3:
                        mostrarMisTickets(sistema, comprador);
                        break;
                    case 4:
                        marketplaceUI.menu();
                        break;
                    case 5:
                        System.out.println("Cerrando sesión...");
                        return; //menu
                }
            } catch (Exception ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        }
    }

    private static void mostrarMisTickets(Sistema sistema, Comprador comprador) {
        System.out.println("\n--- Mis Tickets ---");
        boolean hayTickets = false;
        
        for (Ticket t : sistema.obtenerTicketsPorPropietario(comprador)) {
            System.out.println("ID: " + t.getId() + " | Evento: " + t.getEvento().getNombre() + 
                               " | Estado: " + t.getEstado());
            hayTickets = true;
        }
        if (!hayTickets) System.out.println("No tienes tickets registrados.");
    }
}