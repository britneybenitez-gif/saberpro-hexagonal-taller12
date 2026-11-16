package co.edu.unicauca.saberpro.infrastructure.adapters.out.memory;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/** HashMap en memoria que guarda y recupera copias del agregado, como la persistencia. */
public final class InMemoryQuestionAdapter implements PreguntaRepositoryPort {
    private final Map<UUID, Pregunta> preguntas = Collections.synchronizedMap(new HashMap<>());

    @Override
    public Pregunta save(Pregunta pregunta) {
        Pregunta copia = copiar(pregunta);
        preguntas.put(copia.getId(), copia);
        return copiar(copia);
    }

    @Override
    public Optional<Pregunta> findById(UUID id) {
        Pregunta almacenada = preguntas.get(id);
        return Optional.ofNullable(almacenada).map(InMemoryQuestionAdapter::copiar);
    }

    private static Pregunta copiar(Pregunta pregunta) {
        return new Pregunta(pregunta.getId(), pregunta.getEnunciado(),
                pregunta.getJustificacion(), pregunta.getOpciones(), pregunta.getEstado());
    }
}
