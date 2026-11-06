package example.pasteleria.service;


import example.pasteleria.dto.CategoriaRequest;
import example.pasteleria.model.Categoria;
import example.pasteleria.repository.CategoriaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
        return categoriaRepository.findById(idCategoria)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Categoria no encontrada: " + idCategoria));
    }

    //Eliminar
    public void eliminarCategoria (Integer id){
        Categoria categoria = buscarPorId(id);
        categoriaRepository.delete(categoria);
    }

    //Guardar
    public Categoria guardarCategoria (CategoriaRequest request){
        return categoriaRepository.save(request.toEntity());
    }

    //Actualizar

    public Categoria actualizarCategoria (Integer idCategoria, CategoriaRequest request){
        Categoria categoriaActualizar = buscarPorId(idCategoria);
        categoriaActualizar.setNombre_categoria(request.getNombreCategoria());
         return categoriaRepository.save(categoriaActualizar);
    }
}
