package com.segcaldessof.uniondegamers.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.exception.AsignacionesNoEncontradasException;
import com.segcaldessof.uniondegamers.repository.UsuarioRolRepository;

@Service 
public class UsuarioRolServiceImpl implements UsuarioRolService {

    private final UsuarioRolRepository usuarioRolRepository;

    public UsuarioRolServiceImpl(UsuarioRolRepository usuarioRolRepository){
        this.usuarioRolRepository = usuarioRolRepository;
    }

    // Quita todos los roles de un usuario (sin borrar al usuario)
    @Transactional 
    @Override
    public void quitarTodosLosRoles(Long usuarioId) {
        if(usuarioRolRepository.borrarAsignacionesPorUsuarioId(usuarioId) == 0){
            throw new AsignacionesNoEncontradasException("No se han encontrado asignaciones");
        }
    }

    // Quita un rol específico a un usuario específico
    @Transactional 
    @Override
    public void quitarRol(Long usuarioId, Long rolId) {
        if(usuarioRolRepository.borrarAsignacion(usuarioId, rolId) == 0){
            throw new AsignacionesNoEncontradasException("No se han encontrado asignaciones");
        }
    }

    // Desasigna un rol de todos los usuarios (sin borrar el rol)
    @Transactional 
    @Override
    public void desasignarRolDeTodosLosUsuarios(Long rolId) {
        if(usuarioRolRepository.borrarAsignacionesPorRolId(rolId) == 0){
            throw new AsignacionesNoEncontradasException("No se han encontrado asignaciones");
        }
    }
}
