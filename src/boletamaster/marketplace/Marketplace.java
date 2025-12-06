package boletamaster.marketplace;

import java.util.*;
import java.util.stream.Collectors;
import boletamaster.app.Sistema;
import boletamaster.tiquetes.*;
import boletamaster.usuarios.*;
import boletamaster.marketplace.Oferta.ContraOferta;
import java.io.Serializable;

public class Marketplace implements Serializable{
    private final Sistema sistema;
    private final List<Oferta> ofertas;
    private static final long serialVersionUID = 1L;

    public Marketplace(Sistema sistema) {
        this.sistema = sistema;
        
        this.ofertas = sistema.getRepo().getOfertas(); 
    }

    public List<Oferta> getOfertas() { return ofertas; }
    
    private void registrarLog(String tipoAccion, String detalles) {
        LogRegistro r = new LogRegistro(tipoAccion, detalles);
        sistema.getRepo().addLog(r); // Persist the log
    }

    
    public Oferta publicarOfertaResale(Ticket ticket, Usuario vendedor, double precioVenta) {
        if (ticket == null || vendedor == null || precioVenta <= 0) {
            throw new IllegalArgumentException("Datos de oferta inválidos.");
        }
        if (!(vendedor instanceof Comprador) || !vendedor.getLogin().equals(ticket.getPropietario().getLogin())) {
            throw new IllegalStateException("Solo el propietario del ticket puede venderlo.");
        }
        if (ticket instanceof TicketDeluxe || ticket instanceof TicketMultiple) { 
             throw new IllegalStateException("Tickets Deluxe o paquetes no pueden revenderse.");
        }
        
        Oferta nuevaOferta = new Oferta(ticket, vendedor, precioVenta);
        this.ofertas.add(nuevaOferta);
        sistema.getRepo().addOferta(nuevaOferta);

        registrarLog("OFERTA_CREADA", 
                     String.format("Cliente %s publicó ticket %s por %.2f.", 
                                   vendedor.getLogin(), ticket.getId(), precioVenta));
        return nuevaOferta;
    }

    
    public void borrarOfertaPorCliente(String ofertaId, Usuario cliente) {
        Oferta oferta = buscarOfertaActiva(ofertaId);
        
        if (!oferta.getVendedor().getLogin().equals(cliente.getLogin())) {
            throw new IllegalStateException("Solo el vendedor puede borrar esta oferta.");
        }
        
        oferta.setActiva(false);
        sistema.getRepo().addOferta(oferta);

        registrarLog("OFERTA_BORRADA_CLIENTE", 
                     String.format("Cliente %s borró su oferta ID %s (ticket: %s).", 
                                   cliente.getLogin(), ofertaId, oferta.getTicket().getId()));
    }

    
    public void hacerContraOferta(String ofertaId, Usuario comprador, double precioPropuesto) {
        Oferta oferta = buscarOfertaActiva(ofertaId);
        
        if (precioPropuesto <= 0) {
            throw new IllegalArgumentException("Precio propuesto inválido.");
        }
        if (oferta.getVendedor().getLogin().equals(comprador.getLogin())) {
            throw new IllegalStateException("No puede contraofertar su propia oferta.");
        }
        
        ContraOferta co = new ContraOferta(comprador, precioPropuesto);
        oferta.agregarContraOferta(co);
        sistema.getRepo().addOferta(oferta);

        registrarLog("COUNTER_OFERTA", 
                     String.format("Cliente %s contraofertó %.2f por oferta %s.", 
                                   comprador.getLogin(), precioPropuesto, ofertaId));
    }

    
    public void aceptarOfertaOContraOferta(String ofertaId, Usuario vendedor, String password) {
        Oferta oferta = buscarOfertaActiva(ofertaId);
        Ticket ticket = oferta.getTicket();
        double precioFinal = oferta.getPrecioActual();
        
        if (!oferta.getVendedor().getLogin().equals(vendedor.getLogin()) || !vendedor.checkPassword(password)) {
            throw new IllegalStateException("Credenciales o propiedad de oferta incorrectas.");
        }
        
        Usuario comprador;
        if (oferta.getContraOfertas().isEmpty()) {
            throw new IllegalStateException("No hay contraofertas para aceptar. Use un método de 'Comprar ahora' si aplica.");
        } else {
            comprador = oferta.getContraOfertas().get(oferta.getContraOfertas().size() - 1).getComprador();
        }

        try {
            
            comprador.descontarSaldo(precioFinal);
            vendedor.depositarSaldo(precioFinal);
        } catch (IllegalStateException e) {
            throw new IllegalStateException("Transacción fallida: Saldo insuficiente del comprador.");
        }


        ticket.propietario = comprador;
        ticket.setEstado(TicketEstado.TRANSFERIDO);
        oferta.setActiva(false); 
        

        sistema.getRepo().addOferta(oferta); 
        sistema.getRepo().addTicket(ticket);
        sistema.getRepo().addUsuario(comprador);
        sistema.getRepo().addUsuario(vendedor);

        registrarLog("TRANSACCION_COMPLETADA", 
                     String.format("Venta ticket %s. Vendedor %s -> Comprador %s por %.2f.", 
                                   ticket.getId(), vendedor.getLogin(), comprador.getLogin(), precioFinal));
    }

    public void borrarOfertaPorAdmin(String ofertaId, Administrador admin) {
        Oferta oferta = buscarOfertaActiva(ofertaId);
        
        oferta.setActiva(false);
        sistema.getRepo().addOferta(oferta); 

        registrarLog("OFERTA_BORRADA_ADMIN", 
                     String.format("Admin %s eliminó oferta ID %s (vendida por %s).", 
                                   admin.getLogin(), ofertaId, oferta.getVendedor().getLogin()));
    }

    private Oferta buscarOfertaActiva(String id) {
        return ofertas.stream()
                      .filter(o -> o.getId().equals(id) && o.isActiva())
                      .findFirst()
                      .orElseThrow(() -> new IllegalArgumentException("Oferta no activa o inexistente."));
    }
    
    public List<Oferta> listarOfertasVigentes() {
        return ofertas.stream()
                      .filter(Oferta::isActiva)
                      .collect(Collectors.toList());
    }
}