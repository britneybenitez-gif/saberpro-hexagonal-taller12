package co.edu.unicauca.saberpro.infrastructure.adapters.out.jpa;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

/** Repositorio Spring Data: solo existe dentro de infraestructura. */
public interface SpringDataPreguntaRepository extends JpaRepository<PreguntaJpaEntity, UUID> {}
