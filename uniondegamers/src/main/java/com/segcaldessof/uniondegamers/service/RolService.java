package com.segcaldessof.uniondegamers.service;

import java.util.List;

import com.segcaldessof.uniondegamers.dto.RolNombreRequestDto;
import com.segcaldessof.uniondegamers.dto.RolResponseDto;

public interface RolService {

    List<RolResponseDto> obtenerTodos();

    RolResponseDto obtenerPorId(Long id);

    RolResponseDto modificarNombre(RolNombreRequestDto dto);

    void eliminar(Long id);
}
