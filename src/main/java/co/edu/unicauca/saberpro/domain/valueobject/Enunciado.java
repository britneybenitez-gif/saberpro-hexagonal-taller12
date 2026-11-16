package co.edu.unicauca.saberpro.domain.valueobject;

import co.edu.unicauca.saberpro.domain.exception.ReglaNegocioException;

/** Value Object inmutable: texto obligatorio. */
public record Enunciado(String texto) {
    public Enunciado {
        if (texto == null || texto.isBlank()) {
            throw new ReglaNegocioException("El enunciado es obligatorio");
        }
        texto = texto.trim();
    }
}
