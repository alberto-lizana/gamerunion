package com.segcaldessof.uniondegamers.enums;

public enum RolEnum {
    ROLE_ADMINISTRADOR,
    ROLE_USUARIO,
    ROLE_MODERADOR;

    public String nombreSinPrefijo() {
        return name().substring(5);
    }
}