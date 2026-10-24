package example.pasteleria.service;


import example.pasteleria.model.Producto;
import example.pasteleria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    public List<Producto>listarTodos(){
        return productoRepository.findAll();
    }

}
