package example.pasteleria.repository;

import example.pasteleria.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ProductoRepository extends JpaRepository<Producto,String> {

    

}
