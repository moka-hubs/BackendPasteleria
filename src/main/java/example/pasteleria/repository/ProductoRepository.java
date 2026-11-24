package example.pasteleria.repository;

import example.pasteleria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProductoRepository extends JpaRepository<Producto, String> {

    // Descuenta solo si hay stock suficiente. Es atomico: si otro cliente
    // compro al mismo tiempo, la condicion stock >= cantidad evita ventas
    // por encima del disponible.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Producto p SET p.stock = p.stock - :cantidad " +
           "WHERE p.codigo = :codigo AND p.stock >= :cantidad")
    int descontarStock(@Param("codigo") String codigo,
                       @Param("cantidad") Integer cantidad);

    long countByStock(Integer stock);

    long countByStockBetween(Integer desde, Integer hasta);

    long countByCostoIsNull();

}
