package logica;

import boletamaster.app.Sistema;
import boletamaster.usuarios.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorUsuarios {
    
    private final Sistema sistema; 
    private List<Usuario> usuarios;
    
    public GestorUsuarios(Sistema sistema) {
        this.sistema = sistema;
        
        @SuppressWarnings("unchecked")

        List<Usuario> loadedUsers = (List<Usuario>) sistema.getCore()
                                        .getGestorPersistencia()
                                        .cargarDatos("usuarios.dat");
        
        if (loadedUsers != null && !loadedUsers.isEmpty()) {
            this.usuarios = loadedUsers;
        } else {
            this.usuarios = new ArrayList<>();
            usuarios.add(new Administrador("admin", "admin123", "Administrador Principal"));
        }
    }
    
    public Comprador registrarComprador(String login, String password, String nombre) {
        if (buscarUsuarioPorLogin(login).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Comprador comprador = new Comprador(login, password, nombre);
        usuarios.add(comprador);
        sistema.getRepo().addUsuario(comprador);
        return comprador;
    }
    
    public Organizador registrarOrganizador(String login, String password, String nombre) {
        if (buscarUsuarioPorLogin(login).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Organizador organizador = new Organizador(login, password, nombre);
        usuarios.add(organizador);
        sistema.getRepo().addUsuario(organizador);
        return organizador;
    }
    
    public Optional<Usuario> buscarUsuarioPorLogin(String login) {
        return usuarios.stream()
                      .filter(u -> u.getLogin().equals(login))
                      .findFirst();
    }
    
    
    public Usuario autenticar(String login, String password) {
        Optional<Usuario> usuario = buscarUsuarioPorLogin(login);
        if (usuario.isPresent() && usuario.get().checkPassword(password)) {
            return usuario.get();
        }
        throw new SecurityException("Credenciales inválidas");
    }
    
    public List<Usuario> getUsuarios() {
        return new ArrayList<>(usuarios);
    }
    public Administrador registrarAdministrador(String login, String password, String nombre) {
        if (buscarUsuarioPorLogin(login).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Administrador administrador = new Administrador(login, password, nombre);
        usuarios.add(administrador);
        sistema.getRepo().addUsuario(administrador);
        return administrador;
    }
}
