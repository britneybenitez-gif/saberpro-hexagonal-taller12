package co.edu.unicauca.saberpro.application;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.outbound.NotificacionRevisionPort;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.domain.valueobject.Enunciado;
import co.edu.unicauca.saberpro.domain.valueobject.Justificacion;
import co.edu.unicauca.saberpro.domain.valueobject.OpcionRespuesta;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.UUID;

/** Orquestacion del caso de uso: no conoce HTTP, JPA ni Spring. */
public final class CrearPreguntaService implements CrearPreguntaUseCase {
    private final PreguntaRepositoryPort repositorio;
    private final NotificacionRevisionPort notificaciones;

    public CrearPreguntaService(PreguntaRepositoryPort repositorio,
                                NotificacionRevisionPort notificaciones) {
        this.repositorio = Objects.requireNonNull(repositorio);
        this.notificaciones = Objects.requireNonNull(notificaciones);
    }

    @Override
    public Pregunta crear(String enunciado, String justificacion,
                         List<OpcionRespuesta> opciones) {
        Pregunta pregunta = Pregunta.crear(new Enunciado(enunciado),
                new Justificacion(justificacion), opciones);
        return repositorio.save(pregunta);
    }

    @Override
    public Pregunta buscar(UUID id) {
        return repositorio.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Pregunta no encontrada: " + id));
    }

    @Override
    public Pregunta enviarRevision(UUID id) {
        Pregunta pregunta = buscar(id);
        pregunta.enviarRevision();
        Pregunta guardada = repositorio.save(pregunta);
        notificaciones.notificarPreguntaPendiente(guardada);
        return guardada;
    }
}
