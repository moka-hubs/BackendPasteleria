package example.pasteleria.repository;
import java.time.LocalDate;
import java.util.Optional;

import example.pasteleria.enums.Rol;
import example.pasteleria.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Long> {

    Optional<Usuario> findByCorreoUsuario(String correoUsuario);

    boolean existsByCorreoUsuario(String correoUsuario);

    long countByRolAndFechaRegistroGreaterThanEqual(Rol rol, LocalDate fechaRegistro);

}
