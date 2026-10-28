package com.segcaldessof.uniondegamers.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.segcaldessof.uniondegamers.dto.RolNombreRequestDto;
import com.segcaldessof.uniondegamers.dto.RolResponseDto;
import com.segcaldessof.uniondegamers.service.RolService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/rol")
public class RolController {

    private final RolService rolService;

    public RolController(RolService rolService) {
        this.rolService = rolService;
    }

    @GetMapping("/todos")
    ResponseEntity<List<RolResponseDto>> obtener(){
        return ResponseEntity.ok().body(rolService.obtenerTodos());
    }

    @GetMapping("/{id}")
    ResponseEntity<RolResponseDto> obtener(@PathVariable Long id){
        return ResponseEntity.ok().body(rolService.obtenerPorId(id));
    }

    @PatchMapping("/nombre")
    ResponseEntity<RolResponseDto> modificar(@Valid @RequestBody RolNombreRequestDto dto){
        return ResponseEntity.ok().body(rolService.modificarNombre(dto));
    }

    @DeleteMapping("/borrar/{id}")
    ResponseEntity<Void> eliminar(@PathVariable Long id){
        rolService.eliminar(id);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
