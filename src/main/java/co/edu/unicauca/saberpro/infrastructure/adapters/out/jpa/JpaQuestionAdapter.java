package co.edu.unicauca.saberpro.infrastructure.adapters.out.jpa;

import co.edu.unicauca.saberpro.domain.model.EstadoPregunta;
import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.domain.valueobject.Enunciado;
import co.edu.unicauca.saberpro.domain.valueobject.Justificacion;
import co.edu.unicauca.saberpro.domain.valueobject.OpcionRespuesta;
import java.util.Optional;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

/** Traduccion de agregado puro a JPA y viceversa; JPA nunca se filtra al dominio. */
public class JpaQuestionAdapter implements PreguntaRepositoryPort {
    private final SpringDataPreguntaRepository repositorio;

    public JpaQuestionAdapter(SpringDataPreguntaRepository repositorio) {
        this.repositorio = repositorio;
    }

    @Override
    @Transactional
    public Pregunta save(Pregunta pregunta) {
        PreguntaJpaEntity entidad = new PreguntaJpaEntity();
        entidad.setId(pregunta.getId());
        entidad.setEnunciado(pregunta.getEnunciado().texto());
        entidad.setJustificacion(pregunta.getJustificacion().texto());
        entidad.setEstado(pregunta.getEstado().name());
        entidad.setOpciones(pregunta.getOpciones().stream()
                .map(opcion -> new OpcionEmbeddable(opcion.texto(), opcion.correcta()))
                .toList());
        repositorio.save(entidad);
        return pregunta;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pregunta> findById(UUID id) {
        return repositorio.findById(id).map(entidad ->
                new Pregunta(entidad.getId(),
                        new Enunciado(entidad.getEnunciado()),
                        new Justificacion(entidad.getJustificacion()),
                        entidad.getOpciones().stream()
                                .map(opcion -> new OpcionRespuesta(opcion.getTexto(), opcion.isCorrecta()))
                                .toList(),
                        EstadoPregunta.valueOf(entidad.getEstado())));
    }
}
