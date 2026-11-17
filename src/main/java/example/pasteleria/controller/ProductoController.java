package example.pasteleria.controller;


import example.pasteleria.dto.ProductoRequest;
import example.pasteleria.dto.ProductoResponse;
import example.pasteleria.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@RequestMapping("/api/v1/productos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProductoController {
    @Autowired
    private ProductoService productoService;


    //Listar Todos
    @GetMapping("")
    public List<ProductoResponse> listarTodoss(){
        return productoService.listarTodos().stream().map(ProductoResponse::from).toList();
    }


    //Buscar por codigo
    @GetMapping("/{codigo}")
     public ProductoResponse buscarCorredorPorId(@PathVariable String codigo){
        return ProductoResponse.from(productoService.buscarPorCodigo(codigo));
    }

    //Eliminar por codigo 

    @DeleteMapping ("/{codigo}")
    public void eliminarProducto (@PathVariable String codigo){
        productoService.eliminarProducto(codigo);

    }

    //Agregar Producto 
    @PostMapping("")
    public ProductoResponse agregarProducto (@Valid @RequestBody ProductoRequest productoRequest){
        return ProductoResponse.from(productoService.guardarProducto(productoRequest));
    }

    //Actualizar Producto 
    @PutMapping("/{codigo}")
    public ProductoResponse actualizarProducto(@PathVariable String codigo ,@Valid @RequestBody ProductoRequest productoRequest ){
        return ProductoResponse.from(productoService.actualizarProducto(codigo, productoRequest));
    }

}
