package example.pasteleria.model;

import javax.swing.ImageIcon;

import jakarta.persistence.*;
import lombok.Data;


@Data 
@Entity 
@Table (name = "productos")
public class Producto {

    @Id 
    private String codProducto;

    @Column (nullable = false)
    private ImageIcon imagenProducto;

    @ManyToOne
    @JoinColumn (name = "id_categoria" , nullable = false)
    private Categoria categoriaProducto;

    @Column (nullable = false)
    private String nombreProducto;


    @Column (nullable = false)
    private Integer precioProducto;

    @Column (nullable = false)
    private Integer stockProducto;

    @Column (nullable = false)
    private String descripcionProducto;




    
}
