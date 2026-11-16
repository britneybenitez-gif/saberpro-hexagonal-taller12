package co.edu.unicauca.saberpro.domain.ports.inbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.valueobject.OpcionRespuesta;
import java.util.List;
import java.util.UUID;

/** Puerto primario (entrada) para interactuar con preguntas. */
public interface CrearPreguntaUseCase {
    Pregunta crear(String enunciado, String justificacion, List<OpcionRespuesta> opciones);
    Pregunta buscar(UUID id);
    Pregunta enviarRevision(UUID id);
}
