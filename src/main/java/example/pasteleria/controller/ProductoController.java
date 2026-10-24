package example.pasteleria.controller;


import example.pasteleria.model.Producto;
import example.pasteleria.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;




@RestController
@RequestMapping("/productos")
public class ProductoController {
    @Autowired
    private ProductoService productoService;


    //Listar Todos
    @GetMapping("")
    public List<Producto> listarTodoss(){
        return productoService.listarTodos();
    }


    //Buscar por codigo
    @GetMapping("/{codigo}")
     public Producto buscarCorredorPorId(@PathVariable String codigo){
        return productoService.buscarPorCodigo(codigo);
    }

    //Eliminar por codigo 

    @DeleteMapping ("/{codigo}")
    public void eliminarProducto (@PathVariable String codigo){
        productoService.eliminarProducto(codigo);

    }

    //Agregar Producto 
    @PostMapping("")
    public Producto agregarProducto (@RequestBody Producto producto){
        return productoService.guardaProducto(producto);
    }

    //Actualizar Producto 
    @PutMapping("/{codigo}")
    public Producto actualizarProducto(@PathVariable String codigo ,@RequestBody Producto producto ){
        producto.setCodProducto(codigo);
        return productoService.actualizarProducto(codigo, producto);
    }

    
    
    

}
