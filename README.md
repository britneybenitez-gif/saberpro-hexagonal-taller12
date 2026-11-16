# Taller 12 - Banco de Preguntas Saber PRO: DDD + Arquitectura Hexagonal

Universidad del Cauca · Laboratorio de Ingeniería de Software II · 2026.2

**Objeto del trabajo:** avance obligatorio del Banco Saber PRO, usando **GymClass como referencia**, no como aplicación que se entrega.

## Requisitos y ejecución

- JDK **17** (también compatible con 21), Maven 3.9+ y dependencias descargables desde Maven Central.
- Abrir la carpeta que contiene el archivo `pom.xml`.

```bash
mvn clean test
mvn spring-boot:run
```

URL: `http://localhost:8080/api/preguntas` y consola H2 `http://localhost:8080/h2-console` (JDBC `jdbc:h2:mem:saberprodb`, usuario `sa`, contraseña vacía).

## Cambiar el adaptador SIN tocar domain/

Configurar `app.repository=jpa` o `app.repository=memory` en `src/main/resources/application.properties`, reiniciar y repetir **las mismas peticiones**. Otra opción, sin editar archivos:

```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--app.repository=jpa
mvn spring-boot:run -Dspring-boot.run.arguments=--app.repository=memory
```

El puerto seleccionado es un bean de Spring configurado con `@ConditionalOnProperty`. **El modo memory no usa el adaptador JPA para guardar/consultar preguntas**; el arranque todavía configura Spring Data JPA/H2 porque esas dependencias forman parte de la aplicación demo.

## API (Postman o Thunder Client)

1. `POST /api/preguntas` - `Content-Type: application/json`; usar `ejemplo-pregunta.json`. Retorna 201 y `estado=BORRADOR`.
2. `GET /api/preguntas/{id}` - retorna 200; si no existe, 404.
3. `POST /api/preguntas/{id}/enviar-revision` - retorna 200, `estado=PENDIENTE_REVISION`, y se registra notificación en el log.
4. Si el enunciado está vacío, la justificación tiene menos de 15 caracteres, faltan opciones o hay más de una clave correcta, se obtiene 400 cuando corresponda.

Importar `postman/SaberPRO_Taller12.postman_collection.json` en Postman. Ejecutar del 01 al 05. El archivo guarda temporalmente el identificador `preguntaId` tras la creación.

## DDD y separación de responsabilidades

- **domain/model**: `Pregunta` (Aggregate Root), `EstadoPregunta`; sin Spring ni JPA.
- **domain/valueobject**: tres `record` inmutables con validaciones.
- **domain/ports/inbound**: `CrearPreguntaUseCase` (`crear`, `buscar`, `enviarRevision`).
- **domain/ports/outbound**: `PreguntaRepositoryPort` (`save`, `findById`) y `NotificacionRevisionPort`.
- **application**: `CrearPreguntaService` implementa el puerto de entrada, valida por medio del agregado, persiste y notifica.
- **infrastructure/adapters/in/web**: controlador REST y manejo de excepciones.
- **infrastructure/adapters/out/jpa**: `JpaQuestionAdapter`, entidades JPA y Spring Data, H2.
- **infrastructure/adapters/out/memory**: `InMemoryQuestionAdapter` respaldado por HashMap, guarda instantáneas del agregado.
- **infrastructure/adapters/out/notification**: `LoggingNotificacionAdapter`: **solo log**, no correo ni servicio de mensajería real.
- **infrastructure/config**: configura la inyección de dependencias sin acoplar el dominio.

El borrador puede tener opciones incompletas. **Solo se permite PENDIENTE_REVISION cuando hay exactamente 4 opciones y 1 verdadera**.

## Pruebas y evidencia

- `PreguntaTest`: reglas de dominio, opciones y 15 caracteres (no requiere Spring).
- `IntercambiabilidadTest`: caso de uso con repositorio en memoria, puerto de notificación y copias independientes.
- `ApiJpaIntegrationTest`: API con JPA/H2; prueba bean inyectado, crear, buscar, enviar, errores.
- `ApiMemoryIntegrationTest`: mismas pruebas API con HashMap.

Registrar capturas **reales** de `mvn clean test`, API con ambas configuraciones y, si corresponde, H2 Console. No se incluye ninguna prueba ejecutada ni captura fabricada. Se requiere Java/Maven y dependencias descargadas para correr la suite completa.

Diagramas de arquitectura, agregado y secuencia: [docs/diagramas.md](docs/diagramas.md).

## Entregables pendientes fuera del código

- Subir a **GitHub** (no se debe confundir un ZIP local con un repositorio publicado).
- Trasladar el informe técnico a la plantilla **Formato-Documento.docx** entregada por la docente.
- Insertar los nombres de la pareja y capturas obtenidas durante la ejecución.
