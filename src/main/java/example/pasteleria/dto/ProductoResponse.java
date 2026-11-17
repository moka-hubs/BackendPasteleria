package example.pasteleria.dto;

import example.pasteleria.model.Producto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductoResponse {

    private String codigo;
    private String imagen;
    private String categoria;
    private String nombre;
    private Integer precio;
    private Integer stock;
    private String descripcion;

    public static ProductoResponse from(Producto producto) {
        return new ProductoResponse(
                producto.getCodigo(),
                producto.getImagen(),
                producto.getCategoria() == null ? null : producto.getCategoria().getNombre_categoria(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock(),
                producto.getDescripcion()
        );
    }
}
