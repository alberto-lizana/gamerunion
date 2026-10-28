package com.segcaldessof.uniondegamers.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UsuarioRequestDto(

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    String alias,

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 10, max = 50, message = "La contraseña debe tener entre 10 y 50 caracteres")
    @Pattern(
        regexp = "^(?!\\s)(?=.*[a-z])(?=.*[A-Z])(?=.*[^a-zA-Z0-9])(?<!\\s).*$",
        message = "La contraseña debe contener al menos una mayúscula, una minúscula y un carácter especial, sin espacios al inicio o final"
    )
    String password,

    @NotBlank(message = "El nombre de usuario es obligatorio")
    @Size(min = 3, max = 50, message = "El nombre de usuario debe tener entre 3 y 50 caracteres")
    String nombre,

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(min = 3, max = 50, message = "...")
    String appat,


    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Debe proporcionar un email válido")
    String email
    
) {}
