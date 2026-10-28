package com.segcaldessof.uniondegamers.service;


public interface UsuarioRolService {

    void quitarTodosLosRoles(Long usuarioId);

    void quitarRol(Long usuarioId, Long rolId);

    void desasignarRolDeTodosLosUsuarios(Long rolId);

}
