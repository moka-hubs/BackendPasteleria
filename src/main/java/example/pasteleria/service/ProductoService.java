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


    //Listar
    public List<Producto>listarTodos(){
        return productoRepository.findAll();
    }


    //Buscar Por Codigo 
    public Producto buscarPorCodigo (String codigo){
        return productoRepository.getReferenceById(codigo);

    }


    //Guardar Producto
    public Producto guardaProducto (Producto producto){
        return productoRepository.save(producto);
    }


    //Eliminar Producto 

    public void eliminarProducto (String codigo){
        Producto producto = buscarPorCodigo(codigo);
        productoRepository.delete(producto);
    }


    //Actualizar Producto 
    public Producto actualizarProducto (String codigo, Producto producto){
        Producto productoActualizar = buscarPorCodigo(codigo);
        productoActualizar.setImagenProducto(producto.getImagenProducto());
        productoActualizar.setCategoriaProducto(producto.getCategoriaProducto());
        productoActualizar.setNombreProducto(producto.getNombreProducto());
        productoActualizar.setPrecioProducto(producto.getPrecioProducto());
        productoActualizar.setStockProducto(producto.getStockProducto());
        productoActualizar.setDescripcionProducto(producto.getDescripcionProducto());
        return productoRepository.save(productoActualizar);
    }


}
