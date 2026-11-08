package com.segcaldessof.uniondegamers.config;

import com.segcaldessof.uniondegamers.enums.RolEnum;

public class ErroresConstantes {

    public static final String ROL_NO_ENCONTRADO = "Rol no encontrado.";
    public static final String USUARIO_NO_ENCONTRADO = "No se ha encontrado al usuario.";
    public static final String CREDENCIALES_EXISTENTES = "Las credenciales entregadas ya existen";
    public static final String NO_SE_HAN_ENCONTRADO_ASIGNACIONES = "No se han encontrado asignaciones";
    public static final String EVENTO_NO_ENCONTRADO = "No se encontro el evento";
    public static final String COMENTARIO_NO_ENCONTRADO = "No se ha encontrado el comentario";
    public static final String ACCESO_NO_AUTORIZADO = "Se niega el acceso por falta de coherencia en la información";
    public static final String EVENTO_NO_COINCIDE = "El comentario no pertenece al evento en cuestion";

    public static final String ADMINISTRADOR = RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo();
    public static final String MODERADOR = RolEnum.ROLE_MODERADOR.nombreSinPrefijo();
    public static final String USUARIO = RolEnum.ROLE_USUARIO.nombreSinPrefijo();

}
