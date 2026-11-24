package example.pasteleria.dto;

import example.pasteleria.enums.EstadoPedido;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CambioEstadoRequest {

    @NotNull(message = "El estado es obligatorio")
    private EstadoPedido estado;
}
