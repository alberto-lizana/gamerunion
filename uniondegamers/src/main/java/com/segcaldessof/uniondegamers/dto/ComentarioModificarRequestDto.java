package com.segcaldessof.uniondegamers.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ComentarioModificarRequestDto(

    @NotNull(message = "El identificador del comentario es crucial")
    Long comentarioId,

    @NotBlank(message = "No puede estar vacío")
    String contenido

) {}
