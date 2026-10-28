package com.segcaldessof.uniondegamers.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.segcaldessof.uniondegamers.dto.UsuarioRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;
import com.segcaldessof.uniondegamers.service.UsuarioService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/usuario")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    @GetMapping("/obtener")
    ResponseEntity<List<UsuarioResponseDto>> obtener(){
        return ResponseEntity.ok().body(usuarioService.obtenerTodos());
    }

    @GetMapping("/obtener/{id}")
    ResponseEntity<UsuarioResponseDto> obtener(@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.obtenerPorId(id));
    }
    
    @PostMapping("/crear")
    ResponseEntity<UsuarioResponseDto> crear(@Valid @RequestBody UsuarioRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(dto));
    }
    
    @PatchMapping("/borrar-logico/{id}")
    ResponseEntity<UsuarioResponseDto> borrarL(@PathVariable Long id){
        return ResponseEntity.ok().body(usuarioService.borrarLogico(id));
    }

    @DeleteMapping("/borrar/{id}")
    ResponseEntity<Void> borrarF(@PathVariable Long id){
        usuarioService.borrarPorId(id);
        return ResponseEntity.noContent().build();
    }
}
