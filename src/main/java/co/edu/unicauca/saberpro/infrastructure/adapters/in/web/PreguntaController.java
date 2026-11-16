package co.edu.unicauca.saberpro.infrastructure.adapters.in.web;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.valueobject.OpcionRespuesta;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Adaptador de entrada HTTP: delega las reglas al puerto del caso de uso. */
@RestController
@RequestMapping("/api/preguntas")
public class PreguntaController {
    private final CrearPreguntaUseCase useCase;

    public PreguntaController(CrearPreguntaUseCase useCase) { this.useCase = useCase; }

    public record CrearRequest(String enunciado, String justificacion,
                               List<OpcionRespuesta> opciones) {}

    @PostMapping
    public ResponseEntity<Pregunta> crear(@RequestBody CrearRequest request) {
        Pregunta creada = useCase.crear(request.enunciado(), request.justificacion(), request.opciones());
        return ResponseEntity.status(HttpStatus.CREATED).body(creada);
    }

    @GetMapping("/{id}")
    public Pregunta buscar(@PathVariable UUID id) { return useCase.buscar(id); }

    @PostMapping("/{id}/enviar-revision")
    public Pregunta enviarRevision(@PathVariable UUID id) { return useCase.enviarRevision(id); }
}
