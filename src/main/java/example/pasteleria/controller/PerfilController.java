package example.pasteleria.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import example.pasteleria.dto.PerfilRequest;
import example.pasteleria.dto.PerfilResponse;
import example.pasteleria.service.PerfilService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import jakarta.validation.Valid;


@RestController 
@RequestMapping("/api/v1/perfil")
@CrossOrigin(origins = "http://localhost:5173")
public class PerfilController {
    
    @Autowired 
    private final PerfilService perfilService;

    PerfilController(PerfilService perfilService) {
        this.perfilService = perfilService;
    }
    
    @GetMapping("path")
    public PerfilResponse obtenerPerfil(Authentication a){
        return perfilService.obtenerPerfil(a.getName());
    }
    
    @PutMapping
    public PerfilResponse actiaPerfil(@Valid @RequestBody PerfilRequest r, Authentication a){
            return perfilService.actuallPerfil(a.getName(), r);
    }

}
