package com.segcaldessof.uniondegamers.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.segcaldessof.uniondegamers.service.UsuarioRolService;

@RestController 
@RequestMapping("/usuario-rol")
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

}