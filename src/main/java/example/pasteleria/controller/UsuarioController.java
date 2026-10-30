package example.pasteleria.controller;

import example.pasteleria.model.Usuario;
import example.pasteleria.model.Usuario;
import example.pasteleria.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public List<Usuario> listarTodos (){
        return usuarioService.listarUsuarios();
    }

    //Buscar por id
    @GetMapping("/{idUsuario}")
    public Usuario buscarPorId(@PathVariable Long idUsuario){
        return usuarioService.buscarUsuarioPorId(idUsuario);
    }

    //Actualizar Usuario
    @PutMapping("/{idUsuario}")
    public Usuario actualizarUsuario (@PathVariable Long idUsuario, @RequestBody Usuario usuario){
        usuario.setId(idUsuario);
        return usuarioService.actualizarUsuario(idUsuario,usuario);
    }

    //Eliminar Usuario
    @DeleteMapping("/{idUsuario}")
    public void eliminarUsuario (@PathVariable Long idUsuario){
        usuarioService.eliminarUsuario(idUsuario);
    }

    //Agregar Usuario
    @PostMapping
    public Usuario guardarUsuario (@RequestBody Usuario usuario) {
        return usuarioService.guardaUsuario(usuario);
    }
}
