package example.pasteleria.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CompraRequest {

    @Valid
    @NotEmpty(message = "El carrito no puede estar vacio")
    private List<Item> items;

    @Data
    public static class Item {
        @NotBlank(message = "El codigo del producto es obligatorio")
        private String codigo;

        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        private Integer cantidad;
    }
}
