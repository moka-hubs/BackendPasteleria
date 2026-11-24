package example.pasteleria.model;


import jakarta.persistence.*;
import lombok.Data;


@Data 
@Entity 
@Table (name = "productos")
public class Producto {

    @Id
    @Column(nullable = false)
    private String codigo;

    @Column (nullable = false)
    private String imagen;

    @ManyToOne
    @JoinColumn (name = "id_categoria" , nullable = false)
    private Categoria categoria;

    @Column (nullable = false)
    private String nombre;


    @Column (nullable = false)
    private Integer precio;

    @Column (nullable = false)
    private Integer stock;
    // Costo de elaboracion. Nullable: los productos anteriores quedan sin costo.
    // Solo lo ve el admin (no esta en ProductoResponse, que es publico).
    @Column (nullable = true)
    private Integer costo;

    @Column (nullable = false)
    private String descripcion;




    
}
