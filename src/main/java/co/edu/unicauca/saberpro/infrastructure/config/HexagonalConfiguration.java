package co.edu.unicauca.saberpro.infrastructure.config;

import co.edu.unicauca.saberpro.application.CrearPreguntaService;
import co.edu.unicauca.saberpro.domain.ports.inbound.CrearPreguntaUseCase;
import co.edu.unicauca.saberpro.domain.ports.outbound.NotificacionRevisionPort;
import co.edu.unicauca.saberpro.domain.ports.outbound.PreguntaRepositoryPort;
import co.edu.unicauca.saberpro.infrastructure.adapters.out.jpa.JpaQuestionAdapter;
import co.edu.unicauca.saberpro.infrastructure.adapters.out.jpa.SpringDataPreguntaRepository;
import co.edu.unicauca.saberpro.infrastructure.adapters.out.memory.InMemoryQuestionAdapter;
import co.edu.unicauca.saberpro.infrastructure.adapters.out.notification.LoggingNotificacionAdapter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** La configuracion conecta el nucleo con los adaptadores: domain no depende de Spring. */
@Configuration
public class HexagonalConfiguration {

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "jpa", matchIfMissing = true)
    PreguntaRepositoryPort jpaQuestionPort(SpringDataPreguntaRepository jpa) {
        return new JpaQuestionAdapter(jpa);
    }

    @Bean
    @ConditionalOnProperty(name = "app.repository", havingValue = "memory")
    PreguntaRepositoryPort memoryQuestionPort() {
        return new InMemoryQuestionAdapter();
    }

    @Bean
    NotificacionRevisionPort notificacionRevisionPort() {
        return new LoggingNotificacionAdapter();
    }

    @Bean
    CrearPreguntaUseCase crearPreguntaUseCase(PreguntaRepositoryPort repo,
                                             NotificacionRevisionPort notificaciones) {
        return new CrearPreguntaService(repo, notificaciones);
    }
}
