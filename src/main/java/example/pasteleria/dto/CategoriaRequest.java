package example.pasteleria.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import example.pasteleria.model.Categoria;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CategoriaRequest {

    @JsonProperty("nombre_categoria")
    @NotBlank(message = "El nombre de la categoria es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de la categoria debe tener entre 3 y 50 caracteres")
    private String nombreCategoria;

    public Categoria toEntity() {
        Categoria categoria = new Categoria();
        categoria.setNombre_categoria(nombreCategoria);
        return categoria;
    }
}
