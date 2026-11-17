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

import java.text.Normalizer;
import java.util.List;
import java.util.Locale;

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
        if (productoRepository.existsById(request.getCodigo())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "Producto ya existe: " + request.getCodigo());
        }
        Categoria categoria = obtenerCategoria(request);
        Producto producto = new Producto();
        producto.setCodigo(request.getCodigo());
        producto.setImagen(request.getImagen());
        producto.setCategoria(categoria);
        producto.setNombre(request.getNombre());
        producto.setPrecio(request.getPrecio());
        producto.setStock(request.getStock());
        producto.setDescripcion(request.getDescripcion());
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
        productoActualizar.setImagen(request.getImagen());
        productoActualizar.setCategoria(categoria);
        productoActualizar.setNombre(request.getNombre());
        productoActualizar.setPrecio(request.getPrecio());
        productoActualizar.setStock(request.getStock());
        productoActualizar.setDescripcion(request.getDescripcion());
        return productoRepository.save(productoActualizar);
    }


    private Categoria obtenerCategoria(ProductoRequest request){
        if (request.getCategoria() == null || request.getCategoria().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La categoria es obligatoria");
        }
        String nombreBuscado = normalizar(request.getCategoria());
        return categoriaRepository.findAll().stream()
                .filter(categoria -> normalizar(categoria.getNombre_categoria()).equals(nombreBuscado))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "La categoria no existe"));
    }

    private String normalizar(String texto) {
        return Normalizer.normalize(texto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toLowerCase(Locale.ROOT);
    }


}
