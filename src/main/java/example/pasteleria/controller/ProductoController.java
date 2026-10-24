package example.pasteleria.controller;


import example.pasteleria.model.Producto;
import example.pasteleria.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
