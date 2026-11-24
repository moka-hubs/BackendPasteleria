package example.pasteleria.controller;

import example.pasteleria.dto.DashboardResponse;
import example.pasteleria.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping
    public DashboardResponse obtener() {
        return dashboardService.obtener();
    }

    // { "TC001": 18000, ... } solo para ADMIN (cubierto por /api/v1/dashboard/**).
    @GetMapping("/costos")
    public Map<String, Integer> costos() {
        return dashboardService.obtenerCostos();
    }
}
