package example.pasteleria.repository;

import example.pasteleria.model.PedidoDetalle;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, Long> {

    // Cada fila: [categoria, monto vendido]
    @Query("SELECT d.categoria, SUM(d.subtotal) FROM PedidoDetalle d " +
            "WHERE d.pedido.fecha >= :desde AND d.pedido.fecha < :hasta " +
            "GROUP BY d.categoria ORDER BY SUM(d.subtotal) DESC")
    List<Object[]> ventasPorCategoria(@Param("desde") LocalDateTime desde,
                                      @Param("hasta") LocalDateTime hasta);

    // Cada fila: [nombre, unidades vendidas, ingresos]
    @Query("SELECT MAX(d.nombreProducto), SUM(d.cantidad), SUM(d.subtotal) FROM PedidoDetalle d " +
            "WHERE d.pedido.fecha >= :desde AND d.pedido.fecha < :hasta " +
            "GROUP BY d.codProducto ORDER BY SUM(d.subtotal) DESC")
    List<Object[]> topProductos(@Param("desde") LocalDateTime desde,
                                @Param("hasta") LocalDateTime hasta,
                                Pageable pageable);

    // Ganancia = (precio - costo) x cantidad, solo de lineas que tienen costo registrado.
    @Query("SELECT COALESCE(SUM(d.subtotal - d.costoUnitario * d.cantidad), 0L) FROM PedidoDetalle d " +
            "WHERE d.costoUnitario IS NOT NULL " +
            "AND d.pedido.fecha >= :desde AND d.pedido.fecha < :hasta")
    long sumarGanancia(@Param("desde") LocalDateTime desde,
                       @Param("hasta") LocalDateTime hasta);

    // Ventas de las lineas que tienen costo: base para calcular el margen real.
    @Query("SELECT COALESCE(SUM(d.subtotal), 0L) FROM PedidoDetalle d " +
            "WHERE d.costoUnitario IS NOT NULL " +
            "AND d.pedido.fecha >= :desde AND d.pedido.fecha < :hasta")
    long sumarVentasConCosto(@Param("desde") LocalDateTime desde,
                             @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COALESCE(SUM(d.cantidad), 0L) FROM PedidoDetalle d " +
            "WHERE d.pedido.fecha >= :desde AND d.pedido.fecha < :hasta")
    long sumarUnidades(@Param("desde") LocalDateTime desde,
                       @Param("hasta") LocalDateTime hasta);
}