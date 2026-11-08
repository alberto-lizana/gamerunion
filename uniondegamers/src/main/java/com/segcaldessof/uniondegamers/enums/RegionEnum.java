package com.segcaldessof.uniondegamers.enums;

public enum RegionEnum {

    ARICA_Y_PARINACOTA,
    TARAPACÁ,
    ANTOFAGASTA,
    ATACAMA,
    COQUIMBO,
    VALPARAÍSO,
    METROPOLITANA,
    LIBERTADOR_BERNARDO_O1HIGGINS,
    MAULE,
    ÑUBLE,
    BIOBÍO,
    ARAUCANÍA,
    LOS_RÍOS,
    LOS_LAGOS,
    AYSÉN,
    MAGALLANES;

    public String nombreSinGuiones() {
        return name()
            .replace("_", " ")
            .replace("1", "'");
    }

}
