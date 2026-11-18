package example.pasteleria.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import example.pasteleria.model.Usuario;
import example.pasteleria.dto.PerfilRequest;
import example.pasteleria.dto.PerfilResponse;
import example.pasteleria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PerfilService {

    private final UsuarioRepository usuarioRepository;

    


    public PerfilResponse obtenerPerfil(String correo){
        Usuario u = usuarioRepository.findByCorreoUsuario(correo)
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no encontrado"));
        return mapear(u);
    }

    public PerfilResponse actuallPerfil(String correo, PerfilRequest req){
        Usuario u = usuarioRepository.findByCorreoUsuario(correo)
        .orElseThrow(()-> new ResponseStatusException(HttpStatus.NOT_FOUND, "usuario no encontrado"));
        u.setNombreUsuario(req.getNombreUsuario());
        u.setApellidoUsuario(req.getApellidoUsuario());
        u.setFechaNacUsuario(req.getFechaNacUsuario());
        return mapear(usuarioRepository.save(u));
    }



    private PerfilResponse mapear(Usuario u){
         return new PerfilResponse(u.getId(), u.getNombreUsuario(), u.getApellidoUsuario(),
        u.getCorreoUsuario(), u.getFechaNacUsuario(), u.getCuponBienvenida());
    }
}
