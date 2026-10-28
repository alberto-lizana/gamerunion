package com.segcaldessof.uniondegamers.enums;

public enum RolEnum {
    ROLE_ADMINISTRADOR,
    ROLE_USUARIO;

    public String nombreSinPrefijo() {
        return name().substring(5);
    }
}