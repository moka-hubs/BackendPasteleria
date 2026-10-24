package example.pasteleria.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
@Data 
@Entity 
@Table (name = "categoria")
public class Categoria {

    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Integer idCategoria;

    @Column (nullable = false)
    private String tipo_categoria;


    
}
