package example.pasteleria.dto;

import example.pasteleria.model.Categoria;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data 
public class ProductoRequest {
    

    @NotBlank (message = "El Codigo del producto no puede estar vacio, es obligatorio")
    @Size (min = 3 , max = 6 , message = "El codigo del producto debe tener como minimo 3 caracteres y como maximo 6 caracteres")
    private String codProducto ;

    @NotBlank (message = "La referencia a la imagen del producto no puede estar vacia ")
    private String imagenProducto;


    @NotNull 
    @Valid 
    private Categoria categoriaProducto;

   
    @NotBlank (message = "Es obligatorio que el producto tenga un nombre ")
    @Size (min = 5 , max = 50 , message = "El nombre del producto debe tener como minimo 5 caracteres y como maximo 50 caracteres")
    private String nombreProducto;


    @NotNull (message = "El precio del producto es obligatorio")
    @Min(value = 1000 , message = "El precio del producto no puede ser inferior a $1.000")
    @Max(value = 1600000,message = "El precio del producto excede el monto maximo permitido ")
    private Integer precioProducto;

    @NotNull (message = "El stock del producto es obligatorio")
    @Min(value = 1 , message = "El stock del producto no puede ser inferior a 1  ")
    @Max(value = 100,message = "El stock del producto excede el limite  ")
    private Integer stockProducto;


    @NotBlank (message = "La descripcion del producto no puede estar vacia, es obligatorio")
    @Size (min = 10 , max = 500 , message = "La descripcion del producto debe tener como minimo 10 caracteres y como maximo 500 caracteres")
    private String descripcionProducto;
}
