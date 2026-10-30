package example.pasteleria.service;

import example.pasteleria.model.Usuario;
import example.pasteleria.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

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
        usuarioActualizar.setContraseniaUsuario(usuario.getContraseniaUsuario());
        usuarioActualizar.setFechaNacUsuario(usuario.getFechaNacUsuario());
        usuarioActualizar.setCuponBienvenida(usuario.getCuponBienvenida());
        return usuarioRepository.save(usuarioActualizar);
    }



}
