package example.pasteleria.model;

import example.pasteleria.enums.Rol;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombreUsuario;

    @Column(nullable = false)
    private String apellidoUsuario;

    @Column(nullable = false)
    private String correoUsuario;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(nullable = false)
    private String contraseniaUsuario;

    @Column(nullable = false)
    private Date fechaNacUsuario;

    @Column(nullable = true)
    private String cuponBienvenida;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Rol rol;


}
