package example.pasteleria.service;

import example.pasteleria.dto.CompraRequest;
import example.pasteleria.dto.CompraResponse;
import example.pasteleria.enums.EstadoPedido;
import example.pasteleria.model.Pedido;
import example.pasteleria.model.PedidoDetalle;
import example.pasteleria.model.Producto;
import example.pasteleria.model.Usuario;
import example.pasteleria.repository.PedidoRepository;
import example.pasteleria.repository.ProductoRepository;
import example.pasteleria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private static final ZoneId ZONA = ZoneId.of("America/Santiago");

    private final ProductoRepository productoRepository;
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;

    // Todo ocurre en UNA transaccion: descontar stock + guardar el pedido.
    // Si algo falla (stock insuficiente, usuario inexistente...) se revierte todo.
    @Transactional
    public CompraResponse registrarCompra(String correoUsuario, CompraRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito esta vacio");
        }

        int total = 0;
        List<CompraResponse.Detalle> detalles = new ArrayList<>();
        List<PedidoDetalle> lineas = new ArrayList<>();

        for (CompraRequest.Item item : request.getItems()) {
            String codigo = item.getCodigo();
            Integer cantidad = item.getCantidad();

            if (!productoRepository.existsById(codigo)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Producto no encontrado: " + codigo);
            }

            int filas = productoRepository.descontarStock(codigo, cantidad);
            if (filas == 0) {
                throw new ResponseStatusException(HttpStatus.CONFLICT,
                        "Stock insuficiente para " + codigo);
            }

            // La lectura va despues del UPDATE y con clearAutomatically,
            // asi que trae el stock ya descontado.
            Producto producto = productoRepository.findById(codigo)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                            "Producto no encontrado: " + codigo));

            int subtotal = producto.getPrecio() * cantidad;
            total += subtotal;

            detalles.add(new CompraResponse.Detalle(
                    codigo,
                    producto.getNombre(),
                    cantidad,
                    producto.getStock()
            ));

            // Copia de los datos al momento de la venta (historial inmutable).
            PedidoDetalle linea = new PedidoDetalle();
            linea.setCodProducto(codigo);
            linea.setNombreProducto(producto.getNombre());
            linea.setCategoria(producto.getCategoria() == null
                    ? "Sin categoria"
                    : producto.getCategoria().getNombre_categoria());
            linea.setPrecioUnitario(producto.getPrecio());
            linea.setCostoUnitario(producto.getCosto());
            linea.setCantidad(cantidad);
            linea.setSubtotal(subtotal);
            lineas.add(linea);
        }

        // Se busca al usuario DESPUES de los descuentos: descontarStock limpia
        // el contexto de persistencia (clearAutomatically) y dejaria desconectado
        // a un usuario cargado antes.
        Usuario usuario = usuarioRepository.findByCorreoUsuario(correoUsuario)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                        "Usuario no encontrado"));

        Pedido pedido = new Pedido();
        pedido.setUsuario(usuario);
        pedido.setFecha(LocalDateTime.now(ZONA));
        pedido.setTotal(total);
        pedido.setEstado(EstadoPedido.PENDIENTE);
        lineas.forEach(pedido::agregarDetalle);
        Pedido guardado = pedidoRepository.save(pedido);

        return new CompraResponse(guardado.getId(), total, detalles);
    }
}
