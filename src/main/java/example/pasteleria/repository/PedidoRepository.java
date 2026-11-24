package example.pasteleria.repository;

import example.pasteleria.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    @Query("SELECT COALESCE(SUM(p.total), 0L) FROM Pedido p " +
            "WHERE p.fecha >= :desde AND p.fecha < :hasta")
    long sumarVentas(@Param("desde") LocalDateTime desde,
                     @Param("hasta") LocalDateTime hasta);

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.fecha >= :desde AND p.fecha < :hasta")
    long contarPedidos(@Param("desde") LocalDateTime desde,
                       @Param("hasta") LocalDateTime hasta);

    List<Pedido> findTop5ByOrderByFechaDesc();
}
