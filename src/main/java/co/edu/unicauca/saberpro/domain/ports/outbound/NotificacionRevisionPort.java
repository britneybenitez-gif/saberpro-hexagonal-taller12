package co.edu.unicauca.saberpro.domain.ports.outbound;

import co.edu.unicauca.saberpro.domain.model.Pregunta;

/** Puerto de salida: informa que una pregunta paso a revision. */
public interface NotificacionRevisionPort {
    void notificarPreguntaPendiente(Pregunta pregunta);
}
