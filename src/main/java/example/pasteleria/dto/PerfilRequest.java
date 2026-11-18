package example.pasteleria.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Date;


@Data
public class PerfilRequest {
    @NotBlank @Size(min = 2, max = 50)
    private String nombreUsuario;

    @NotBlank @Size(min = 2, max = 50)
    private String apellidoUsuario;

    @NotNull @Past
    private Date fechaNacUsuario;
}