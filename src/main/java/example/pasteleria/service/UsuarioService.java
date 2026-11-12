package example.pasteleria.service;

import example.pasteleria.enums.Rol;
import example.pasteleria.model.Usuario;
import example.pasteleria.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    //Listar
    public List<Usuario> listarUsuarios(){
        return usuarioRepository.findAll();
    }


    //Buscar Por Id
    public Usuario buscarUsuarioPorId (Long id){
        return usuarioRepository.getReferenceById(id);

    }


    //Guardar usuario
    public Usuario guardaUsuario (Usuario usuario){
        if (usuario.getRol() == null) {
            usuario.setRol(Rol.CLIENTE);
        }
        usuario.setContraseniaUsuario(passwordEncoder.encode(usuario.getContraseniaUsuario()));
        return usuarioRepository.save(usuario);
    }


    //Eliminar Usuario

    public void eliminarUsuario (Long id){
        Usuario usuario = buscarUsuarioPorId(id);
        usuarioRepository.delete(usuario);
    }


    //Actualizar Usuario
    public Usuario actualizarUsuario (Long id, Usuario usuario){
        Usuario usuarioActualizar = buscarUsuarioPorId(id);
        usuarioActualizar.setNombreUsuario(usuario.getNombreUsuario());
        usuarioActualizar.setApellidoUsuario(usuario.getApellidoUsuario());
        usuarioActualizar.setCorreoUsuario(usuario.getCorreoUsuario());
        if (usuario.getContraseniaUsuario() != null && !usuario.getContraseniaUsuario().isBlank()) {
            usuarioActualizar.setContraseniaUsuario(passwordEncoder.encode(usuario.getContraseniaUsuario()));
        }
        usuarioActualizar.setFechaNacUsuario(usuario.getFechaNacUsuario());
        usuarioActualizar.setCuponBienvenida(usuario.getCuponBienvenida());
        return usuarioRepository.save(usuarioActualizar);
    }



}
