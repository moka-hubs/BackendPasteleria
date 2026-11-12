package example.pasteleria.repository;
import java.util.Optional;
import example.pasteleria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByUsername(String username);

    boolean existsByUsername(String username);

}
