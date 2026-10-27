package example.pasteleria.service;


import example.pasteleria.model.Categoria;
import example.pasteleria.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    //Listar
    public List<Categoria>buscartodos(){
        return categoriaRepository.findAll();
    }


    //Buscar por id
    public Categoria buscarPorId(Integer idCategoria){
        return categoriaRepository.findById(idCategoria).get();
    }

    //Eliminar
    public void eliminarCategoria (Integer id){
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }

    //Guardar
    public Categoria guardarCategoria (Categoria categoria){
        return categoriaRepository.save(categoria);
    }

    //Actualizar

    public Categoria actualizarCategoria (Integer idCategoria, Categoria categoria){
        Categoria categoriaActualizar = buscarPorId(idCategoria);
        categoriaActualizar.setNombre_categoria(categoria.getNombre_categoria());
         return categoriaRepository.save(categoriaActualizar);
    }
}
