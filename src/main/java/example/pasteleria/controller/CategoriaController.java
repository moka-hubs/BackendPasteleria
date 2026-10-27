package example.pasteleria.controller;


import example.pasteleria.model.Categoria;
import example.pasteleria.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/categorias")
public class CategoriaController {
    @Autowired
    private CategoriaService categoriaService;

    //Listar Todos
    @GetMapping("")
    public List<Categoria> listarTodos (){
        return categoriaService.buscartodos();
    }

    //Buscar por id
    @GetMapping("/{idCategoria}")
    public Categoria buscarPorCodigo(@PathVariable Integer idCategoria){
        return categoriaService.buscarPorId(idCategoria);
    }

    //Actualizar Categoria
    @PutMapping("/{codigo}")
    public Categoria actualizarCategoria (@PathVariable Integer idCategoria, @RequestBody Categoria categoria){
        categoria.setId_categoria(idCategoria);
        return categoriaService.actualizarCategoria(idCategoria,categoria);
    }

    //Eliminar Categoria
    @DeleteMapping("/{codigo}")
    public void eliminarCategoria (@PathVariable Integer idCategoria){
        categoriaService.eliminarCategoria(idCategoria);
    }

    //Agregar Categoria
    @PostMapping
    public Categoria guardarCategoria (@RequestBody Categoria categoria) {
        return categoriaService.guardarCategoria(categoria);
    }
}
