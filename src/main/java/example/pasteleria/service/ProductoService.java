package example.pasteleria.service;


import example.pasteleria.dto.ProductoRequest;
import example.pasteleria.model.Categoria;
import example.pasteleria.model.Producto;
import example.pasteleria.repository.CategoriaRepository;
import example.pasteleria.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProductoService {
    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;


    //Listar
    public List<Producto>listarTodos(){
        return productoRepository.findAll();
    }


    //Buscar Por Codigo 
    public Producto buscarPorCodigo (String codigo){
        return productoRepository.findById(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Producto no encontrado: " + codigo));
    }


    //Guardar Producto
    public Producto guardarProducto(ProductoRequest request){
        if (productoRepository.existsById(request.getCodProducto())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Producto ya existe: " + request.getCodProducto());
        }
        Categoria categoria = obtenerCategoria(request);
        Producto producto = new Producto();
        producto.setCodProducto(request.getCodProducto());
        producto.setImagenProducto(request.getImagenProducto());
        producto.setCategoriaProducto(categoria);
        producto.setNombreProducto(request.getNombreProducto());
        producto.setPrecioProducto(request.getPrecioProducto());
        producto.setStockProducto(request.getStockProducto());
        producto.setDescripcionProducto(request.getDescripcionProducto());
        return productoRepository.save(producto);
    }


    //Eliminar Producto 

    public void eliminarProducto (String codigo){
        Producto producto = buscarPorCodigo(codigo);
        productoRepository.delete(producto);
    }


    //Actualizar Producto 
    public Producto actualizarProducto (String codigo, ProductoRequest request){
        Producto productoActualizar = buscarPorCodigo(codigo);
        Categoria categoria = obtenerCategoria(request);
        productoActualizar.setImagenProducto(request.getImagenProducto());
        productoActualizar.setCategoriaProducto(categoria);
        productoActualizar.setNombreProducto(request.getNombreProducto());
        productoActualizar.setPrecioProducto(request.getPrecioProducto());
        productoActualizar.setStockProducto(request.getStockProducto());
        productoActualizar.setDescripcionProducto(request.getDescripcionProducto());
        return productoRepository.save(productoActualizar);
    }


    private Categoria obtenerCategoria(ProductoRequest request){
        if (request.getCategoriaProducto() == null
                || request.getCategoriaProducto().getId_categoria() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La categoria es obligatoria");
        }
        return categoriaRepository.findById(request.getCategoriaProducto().getId_categoria())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La categoria no existe"));
    }


}
