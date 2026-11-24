package example.pasteleria.service;

import example.pasteleria.dto.DashboardResponse;
import example.pasteleria.enums.Rol;
import example.pasteleria.model.Pedido;
import example.pasteleria.model.Producto;
import example.pasteleria.repository.PedidoDetalleRepository;
import example.pasteleria.repository.PedidoRepository;
import example.pasteleria.repository.ProductoRepository;
import example.pasteleria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private static final ZoneId ZONA = ZoneId.of("America/Santiago");
    // Mismo criterio que el badge de "stock bajo" del panel (3 o menos).
    private static final int STOCK_BAJO = 3;
    // Rango que abarca todas las ventas (para los totales historicos).
    private static final LocalDateTime DESDE_SIEMPRE = LocalDateTime.of(2000, 1, 1, 0, 0);
    private static final LocalDateTime HASTA_SIEMPRE = LocalDateTime.of(2100, 1, 1, 0, 0);

    private final PedidoRepository pedidoRepository;
    private final PedidoDetalleRepository detalleRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProductoRepository productoRepository;

    @Transactional(readOnly = true)
    public DashboardResponse obtener() {
        YearMonth mes = YearMonth.now(ZONA);
        LocalDateTime desde = mes.atDay(1).atStartOfDay();
        LocalDateTime hasta = mes.plusMonths(1).atDay(1).atStartOfDay();
        LocalDateTime desdeAnterior = mes.minusMonths(1).atDay(1).atStartOfDay();

        long pedidosMes = pedidoRepository.contarPedidos(desde, hasta);
        long ventasMes = pedidoRepository.sumarVentas(desde, hasta);
        long ticket = pedidosMes == 0 ? 0 : Math.round((double) ventasMes / pedidosMes);

        DashboardResponse.Kpis kpis = new DashboardResponse.Kpis(
                // Historico
                pedidoRepository.sumarVentas(DESDE_SIEMPRE, HASTA_SIEMPRE),
                detalleRepository.sumarGanancia(DESDE_SIEMPRE, HASTA_SIEMPRE),
                detalleRepository.sumarVentasConCosto(DESDE_SIEMPRE, HASTA_SIEMPRE),
                pedidoRepository.contarPedidos(DESDE_SIEMPRE, HASTA_SIEMPRE),
                detalleRepository.sumarUnidades(DESDE_SIEMPRE, HASTA_SIEMPRE),
                // Mes en curso y mes anterior
                ventasMes,
                pedidoRepository.sumarVentas(desdeAnterior, desde),
                detalleRepository.sumarGanancia(desde, hasta),
                detalleRepository.sumarGanancia(desdeAnterior, desde),
                pedidosMes,
                pedidoRepository.contarPedidos(desdeAnterior, desde),
                ticket,
                usuarioRepository.countByRolAndFechaRegistroGreaterThanEqual(Rol.CLIENTE, mes.atDay(1)),
                // Inventario
                productoRepository.countByStock(0),
                productoRepository.countByStockBetween(1, STOCK_BAJO),
                productoRepository.countByCostoIsNull());

        List<DashboardResponse.VentaCategoria> categorias = detalleRepository
                .ventasPorCategoria(desde, hasta).stream()
                .map(f -> new DashboardResponse.VentaCategoria(
                        (String) f[0], ((Number) f[1]).longValue()))
                .toList();

        List<DashboardResponse.TopProducto> top = detalleRepository
                .topProductos(desde, hasta, PageRequest.of(0, 5)).stream()
                .map(f -> new DashboardResponse.TopProducto(
                        (String) f[0], ((Number) f[1]).longValue(), ((Number) f[2]).longValue()))
                .toList();

        List<DashboardResponse.PedidoReciente> recientes = pedidoRepository
                .findTop5ByOrderByFechaDesc().stream()
                .map(this::aPedidoReciente)
                .toList();

        return new DashboardResponse(kpis, categorias, top, recientes);
    }

    // Costos por codigo de producto. Solo lo expone un endpoint de ADMIN,
    // porque el catalogo publico no debe mostrar los costos.
    @Transactional(readOnly = true)
    public Map<String, Integer> obtenerCostos() {
        Map<String, Integer> costos = new HashMap<>();
        for (Producto p : productoRepository.findAll()) {
            if (p.getCosto() != null) {
                costos.put(p.getCodigo(), p.getCosto());
            }
        }
        return costos;
    }

    private DashboardResponse.PedidoReciente aPedidoReciente(Pedido p) {
        String cliente = p.getUsuario().getNombreUsuario() + " " + p.getUsuario().getApellidoUsuario();
        return new DashboardResponse.PedidoReciente(
                p.getId(), cliente, p.getTotal(), p.getEstado().name(), p.getFecha());
    }
}
