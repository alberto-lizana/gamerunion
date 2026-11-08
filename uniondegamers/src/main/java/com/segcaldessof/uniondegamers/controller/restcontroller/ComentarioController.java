package com.segcaldessof.uniondegamers.controller.restcontroller;

import org.springframework.data.domain.Sort;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.segcaldessof.uniondegamers.dto.ComentarioCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.ComentarioModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.ComentarioResponseDto;
import com.segcaldessof.uniondegamers.service.ComentarioService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/comentario")
public class ComentarioController {

    private final ComentarioService comentarioService;

    public ComentarioController(ComentarioService comentarioService){
        this.comentarioService = comentarioService;
    }

    @GetMapping("/obtener-todos")
    ResponseEntity<Page<ComentarioResponseDto>> obtenerTodos(
        @PageableDefault(
            page = 0,
            size = 10,
            sort = "fechaCreacion",
            direction = Sort.Direction.DESC
        ) Pageable pageable
    ){
        return ResponseEntity.ok().body(comentarioService.obtenerTodos(pageable));
    }    

    @GetMapping("/obtener-{id}")
    ResponseEntity<ComentarioResponseDto> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(comentarioService.obtenerPorId(id));
    }

    @GetMapping("/obtener-por-evento-{eventoId}")
    ResponseEntity<Page<ComentarioResponseDto>> obtenerPorEventoId(@PathVariable Long eventoId, Pageable pageable){
        return ResponseEntity.ok().body(comentarioService.obtenerPorEventoId(eventoId, pageable));
    }

    @PostMapping("/crear")
    ResponseEntity<ComentarioResponseDto> crear(
        @Valid @RequestBody ComentarioCrearRequestDto dto,
        Authentication authentication
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(comentarioService.crear(dto, authentication));
    }

    @PutMapping("/modificar")
    ResponseEntity<ComentarioResponseDto> crear(@Valid @RequestBody ComentarioModificarRequestDto dto, Authentication authentication){
        return ResponseEntity.ok().body(comentarioService.modificar(dto, authentication));
    }

    @DeleteMapping("/eliminar-{id}")
    ResponseEntity<Void> eliminar(@PathVariable Long id){
            comentarioService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
