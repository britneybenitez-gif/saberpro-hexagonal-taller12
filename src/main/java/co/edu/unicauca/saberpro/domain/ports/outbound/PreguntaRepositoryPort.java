package co.edu.unicauca.saberpro.domain.ports.outbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import java.util.Optional;
import java.util.UUID;

/** Puerto secundario (salida) de persistencia, independiente de JPA. */
public interface PreguntaRepositoryPort {
    Pregunta save(Pregunta pregunta);
    Optional<Pregunta> findById(UUID id);
}
