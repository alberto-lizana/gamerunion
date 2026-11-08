package com.segcaldessof.uniondegamers.dto;

import java.time.LocalDateTime;

import com.segcaldessof.uniondegamers.enums.RegionEnum;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EventoCrearRequestDto(

    @Size(min=10, max=150, message = "La cantidad mínima de caracteres, para el titulo, es de 10 y la máxima 150")
    @NotBlank(message = "El titulo no puede ser nulo")
    String titulo, 

    @FutureOrPresent(message = "La fecha de inicio del evento debe estar en el presente o en el futuro") 
    LocalDateTime fechaInicioEvento,

    @FutureOrPresent(message = "La fecha de término del evento debe estar en el futuro") 
    LocalDateTime fechaTerminoEvento,

    @NotBlank(message = "La ubicacion o dirección es un campo obligatorio") 
    @Size(max=500, message = "La dirección o ubicación del evento es información crucial")
    String ubicacion,

    @NotNull(message = "La region es información crucial")
    RegionEnum region

) {}
