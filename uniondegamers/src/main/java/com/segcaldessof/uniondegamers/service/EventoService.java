package com.segcaldessof.uniondegamers.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

import com.segcaldessof.uniondegamers.dto.EventoCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoResponseDto;
import com.segcaldessof.uniondegamers.dto.RegionesResponseDto;

public interface EventoService {

    List<RegionesResponseDto> obtenerRegiones();

    Page<EventoResponseDto> obtenerTodos(Pageable pageable);

    Page<EventoResponseDto> obtenerTodosActivos(Pageable pageable);

    EventoResponseDto obtenerPorId(Long id);

    EventoResponseDto obtenerActivoPorId(Long id);

    EventoResponseDto crear(EventoCrearRequestDto dto, Authentication authentication);

    EventoResponseDto modificar(EventoModificarRequestDto dto);

    void borrarLogico(Long id);

    void borrarFisico(Long id);

}
