package example.pasteleria.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor 
@Data 
public class AuthResponse {

    private String token;
    private String tipo;
    private long expiracion;
}
