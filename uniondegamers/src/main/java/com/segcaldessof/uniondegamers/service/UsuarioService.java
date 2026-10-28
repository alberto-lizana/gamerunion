package com.segcaldessof.uniondegamers.service;

import java.util.List;

import com.segcaldessof.uniondegamers.dto.UsuarioRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;

public interface UsuarioService {

    List<UsuarioResponseDto> obtenerTodos();

    UsuarioResponseDto obtenerPorId(Long id);

    UsuarioResponseDto crear(UsuarioRequestDto dto);

    UsuarioResponseDto borrarLogico(Long id);

    void borrarPorId(Long id);

}