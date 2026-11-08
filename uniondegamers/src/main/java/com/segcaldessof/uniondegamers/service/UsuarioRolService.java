package com.segcaldessof.uniondegamers.service;

import com.segcaldessof.uniondegamers.dto.AsignarRolRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;

public interface UsuarioRolService {

    void quitarTodosLosRoles(Long usuarioId);

    void quitarRol(Long usuarioId, Long rolId);

    void desasignarRolDeTodosLosUsuarios(Long rolId);

    UsuarioResponseDto asignarRol(AsignarRolRequestDto dto);

}
