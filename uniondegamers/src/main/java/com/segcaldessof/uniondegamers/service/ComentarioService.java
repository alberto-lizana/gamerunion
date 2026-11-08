package com.segcaldessof.uniondegamers.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.segcaldessof.uniondegamers.dto.ComentarioCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.ComentarioModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.ComentarioResponseDto;

public interface ComentarioService {

    Page<ComentarioResponseDto> obtenerTodos(Pageable pageable);

    ComentarioResponseDto obtenerPorId(Long id);

    Page<ComentarioResponseDto> obtenerPorEventoId(Long eventoId, Pageable pageable);

    ComentarioResponseDto crear(ComentarioCrearRequestDto dto, Authentication authentication);

    ComentarioResponseDto modificar(ComentarioModificarRequestDto dto, Authentication authentication);
    
    void eliminar(Long id);
}
