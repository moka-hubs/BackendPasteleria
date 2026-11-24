package example.pasteleria.dto;

import java.time.LocalDateTime;
import java.util.List;

public record DashboardResponse(
        Kpis kpis,
        List<VentaCategoria> ventasPorCategoria,
        List<TopProducto> topProductos,
        List<PedidoReciente> pedidosRecientes) {

    public record Kpis(
            // Historico
            long ventasTotal,
            long gananciaTotal,
            long ventasConCostoTotal,   // base para el margen: ganancia / ventasConCosto
            long pedidosTotal,
            long unidadesTotal,
            // Mes en curso y mes anterior
            long ventasMes,
            long ventasMesAnterior,
            long gananciaMes,
            long gananciaMesAnterior,
            long pedidosMes,
            long pedidosMesAnterior,
            long ticketPromedio,
            long clientesNuevosMes,
            // Inventario
            long productosAgotados,
            long productosStockBajo,
            long productosSinCosto) {}

    public record VentaCategoria(String categoria, long monto) {}

    public record TopProducto(String nombre, long unidades, long ingresos) {}

    public record PedidoReciente(Long id, String cliente, int total,
                                 String estado, LocalDateTime fecha) {}
}
