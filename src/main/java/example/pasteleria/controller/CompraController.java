package example.pasteleria.controller;

import example.pasteleria.dto.CompraRequest;
import example.pasteleria.dto.CompraResponse;
import example.pasteleria.service.CompraService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/compras")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @PostMapping
    public CompraResponse registrar(@Valid @RequestBody CompraRequest request) {
        return compraService.registrarCompra(request);
    }
}
