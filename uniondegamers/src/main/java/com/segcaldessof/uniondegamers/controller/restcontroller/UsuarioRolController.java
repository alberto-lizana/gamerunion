package com.segcaldessof.uniondegamers.controller.restcontroller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.segcaldessof.uniondegamers.dto.AsignarRolRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;
import com.segcaldessof.uniondegamers.service.UsuarioRolService;

import jakarta.validation.Valid;

@RestController 
@RequestMapping("/api/usuario-rol")
public class UsuarioRolController {

    private final UsuarioRolService usuarioRolService;

    public UsuarioRolController(UsuarioRolService usuarioRolService){
        this.usuarioRolService = usuarioRolService;
    }

    // Quita todos los roles de un usuario (sin borrar al usuario)
    @DeleteMapping("/{usuarioId}/roles")
    public ResponseEntity<Void> quitarTodosLosRoles(@PathVariable Long usuarioId) {
        usuarioRolService.quitarTodosLosRoles(usuarioId);
        return ResponseEntity.noContent().build();
    }

    // Quita un rol específico a un usuario específico
    @DeleteMapping("/roles/{usuarioId}")
    public ResponseEntity<Void> quitarRol(@PathVariable Long usuarioId, @PathVariable Long rolId) {
        usuarioRolService.quitarRol(usuarioId, rolId);
        return ResponseEntity.noContent().build();
    }

    // Desasigna un rol de todos los usuarios (sin borrar el rol)
    @DeleteMapping("/roles/{rolId}/usuarios")
    public ResponseEntity<Void> desasignarRolDeTodosLosUsuarios(@PathVariable Long rolId) {
        usuarioRolService.desasignarRolDeTodosLosUsuarios(rolId);
        return ResponseEntity.noContent().build();
    }

    // Asignar un rol a un usuario.
    @PostMapping("/asignar-rol")
    public ResponseEntity<UsuarioResponseDto> asignarRol(@Valid @RequestBody AsignarRolRequestDto dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioRolService.asignarRol(dto));
    }

}