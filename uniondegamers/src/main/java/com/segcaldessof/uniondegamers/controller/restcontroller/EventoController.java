package com.segcaldessof.uniondegamers.controller.restcontroller;

import org.springframework.data.domain.Sort;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.segcaldessof.uniondegamers.dto.EventoCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoResponseDto;
import com.segcaldessof.uniondegamers.dto.RegionesResponseDto;
import com.segcaldessof.uniondegamers.service.EventoService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/evento")
public class EventoController {

    private final EventoService eventoService;

    public EventoController(
        EventoService eventoService
    ){
        this.eventoService = eventoService;
    }

    @GetMapping("/obtener-regiones")
    ResponseEntity<List<RegionesResponseDto>> obtenerRegiones(){
        return ResponseEntity.ok().body(eventoService.obtenerRegiones());
    }

    @GetMapping("/obtener-todos")
    ResponseEntity<Page<EventoResponseDto>> obtenerTodos(
        @PageableDefault(
            page = 0,
            size = 12,
            sort = "fechaCreacion",
            direction = Sort.Direction.DESC
        ) Pageable pageable
    ){
        return ResponseEntity.ok().body(eventoService.obtenerTodos(pageable));
    }

    @GetMapping("/obtener-todos-activos")
    ResponseEntity<Page<EventoResponseDto>> obtenerTodosActivos(
        @PageableDefault(
            page = 0,
            size = 12,
            sort = "fechaCreacion",
            direction = Sort.Direction.DESC
        ) Pageable pageable
    ){
        return ResponseEntity.ok().body(eventoService.obtenerTodosActivos(pageable));
    }
    
    @GetMapping("/obtener-{id}")
    ResponseEntity<EventoResponseDto> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(eventoService.obtenerPorId(id));
    }

    @GetMapping("/obtener-activos-{id}")
    ResponseEntity<EventoResponseDto> obtenerActivoPorId(@PathVariable Long id){
        return ResponseEntity.ok().body(eventoService.obtenerActivoPorId(id));
    }

    @PostMapping("/crear")
    ResponseEntity<EventoResponseDto> crear(
        @Valid @RequestBody EventoCrearRequestDto dto, 
        Authentication authentication
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(eventoService.crear(dto, authentication));
    }

    @PutMapping("/modificar")
    ResponseEntity<EventoResponseDto> modificar(@RequestBody EventoModificarRequestDto dto){
        return ResponseEntity.ok().body(eventoService.modificar(dto));
    }


    @PatchMapping("/borrar-logico-{id}")
    ResponseEntity<Void> borrarLogico(@PathVariable Long id){
        eventoService.borrarLogico(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @DeleteMapping("/borrar-fisico-{id}")
    ResponseEntity<Void> borrarFisico(@PathVariable Long id){
        eventoService.borrarFisico(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
