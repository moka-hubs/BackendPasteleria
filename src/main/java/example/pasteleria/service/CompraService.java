package example.pasteleria.service;

import example.pasteleria.dto.CompraRequest;
import example.pasteleria.dto.CompraResponse;
import example.pasteleria.model.Producto;
import example.pasteleria.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CompraService {

    private final ProductoRepository productoRepository;

    @Transactional
    public CompraResponse registrarCompra(CompraRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El carrito esta vacio");
        }

        int total = 0;
        List<CompraResponse.Detalle> detalles = new ArrayList<>();

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

            total += producto.getPrecio() * cantidad;
            detalles.add(new CompraResponse.Detalle(
                    codigo,
                    producto.getNombre(),
                    cantidad,
                    producto.getStock()
            ));
        }

        return new CompraResponse(total, detalles);
    }
}
