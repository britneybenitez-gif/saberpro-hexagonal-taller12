package co.edu.unicauca.saberpro.infrastructure.adapters.out.notification;

import co.edu.unicauca.saberpro.domain.model.Pregunta;
import co.edu.unicauca.saberpro.domain.ports.outbound.NotificacionRevisionPort;
import java.util.logging.Logger;

/** Adaptador demostrativo: registra la notificacion en el log, sin servicios externos. */
public final class LoggingNotificacionAdapter implements NotificacionRevisionPort {
    private static final Logger LOGGER = Logger.getLogger(LoggingNotificacionAdapter.class.getName());

    @Override
    public void notificarPreguntaPendiente(Pregunta pregunta) {
        LOGGER.info(() -> "Pregunta enviada a revision: " + pregunta.getId());
    }
}
