package main.java.com.sistema.asignaturas_servicio.com.sistema.asignaturas.controller;

import com.sistema.asignaturas.model.Asignatura;
import com.sistema.asignaturas.service.AsignaturaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {

    private final AsignaturaService service;

    public AsignaturaController(AsignaturaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Asignatura> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asignatura> obtener(@PathVariable Long id) {
        return service.obtener(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Asignatura crear(@RequestBody Asignatura asignatura) {
        return service.crear(asignatura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
