package logica;

import boletamaster.app.Sistema;
import boletamaster.usuarios.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class GestorUsuarios {
    
    private final Sistema sistema;  
    
    public GestorUsuarios(Sistema sistema) {
        this.sistema = sistema;
        
    }
    
    public Comprador registrarComprador(String login, String password, String nombre) {
        if (buscarUsuarioPorLogin(login).isPresent()) { 
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Comprador comprador = new Comprador(login, password, nombre);
        sistema.getRepo().addUsuario(comprador); 
        return comprador;
    }
    
    public Organizador registrarOrganizador(String login, String password, String nombre) {
        if (buscarUsuarioPorLogin(login).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Organizador organizador = new Organizador(login, password, nombre);
        sistema.getRepo().addUsuario(organizador);
        return organizador;
    }
    
    public Optional<Usuario> buscarUsuarioPorLogin(String login) {
        return sistema.getRepo().getUsuarios().stream()
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
        return sistema.getRepo().getUsuarios();
    }
    
    public Administrador registrarAdministrador(String login, String password, String nombre) {
        if (buscarUsuarioPorLogin(login).isPresent()) {
            throw new IllegalArgumentException("El usuario ya existe");
        }
        Administrador administrador = new Administrador(login, password, nombre);
        sistema.getRepo().addUsuario(administrador);
        return administrador;
    }
}