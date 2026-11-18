package example.pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;


@Data 
@AllArgsConstructor 
public class PerfilResponse {
    private Long id;
    private String nombreUsuario;
    private String apellidoUsuario;
    private String correoUsuario;
    private Date fechaNacUsuario;
    private String cuponBienvenida;
}
