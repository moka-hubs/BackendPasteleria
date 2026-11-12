package example.pasteleria.service;

import example.pasteleria.dto.AuthResponse;
import example.pasteleria.dto.LoginRequest;
import example.pasteleria.dto.RegisterRequest;
import example.pasteleria.enums.Rol;
import example.pasteleria.model.Usuario;
import example.pasteleria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final String CARACTERES_CUPON = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final SecureRandom RANDOM = new SecureRandom();

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreoUsuario(request.getCorreo().trim())
                .orElseThrow(() -> new BadCredentialsException("Credenciales invalidas"));

        if (!passwordEncoder.matches(request.getPassword(), usuario.getContraseniaUsuario())) {
            throw new BadCredentialsException("Credenciales invalidas");
        }

        return construirRespuesta(usuario);
    }

    public AuthResponse registrar(RegisterRequest request) {
        String correo = request.getCorreoUsuario().trim().toLowerCase();

        if (usuarioRepository.existsByCorreoUsuario(correo)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe una cuenta con ese correo");
        }

        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(request.getNombreUsuario().trim());
        usuario.setApellidoUsuario(request.getApellidoUsuario().trim());
        usuario.setCorreoUsuario(correo);
        usuario.setContraseniaUsuario(passwordEncoder.encode(request.getContraseniaUsuario()));
        usuario.setFechaNacUsuario(request.getFechaNacUsuario());
        usuario.setCuponBienvenida(generarCuponBienvenida());
        // El rol nunca viene del cliente: todo registro publico es CLIENTE.
        usuario.setRol(Rol.CLIENTE);

        return construirRespuesta(usuarioRepository.save(usuario));
    }

    private AuthResponse construirRespuesta(Usuario usuario) {
        String token = jwtService.generarToken(usuario.getCorreoUsuario(), usuario.getRol());
        return new AuthResponse(token, "Bearer", jwtService.getExpiracion());
    }

    private String generarCuponBienvenida() {
        StringBuilder cupon = new StringBuilder("BIENVENIDO-");
        for (int i = 0; i < 6; i++) {
            cupon.append(CARACTERES_CUPON.charAt(RANDOM.nextInt(CARACTERES_CUPON.length())));
        }
        return cupon.toString();
    }
}
