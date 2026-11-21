package example.pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CompraResponse {

    private Integer total;
    private List<Detalle> items;

    @Data
    @AllArgsConstructor
    public static class Detalle {
        private String codigo;
        private String nombre;
        private Integer cantidad;
        private Integer stockRestante;
    }
}
