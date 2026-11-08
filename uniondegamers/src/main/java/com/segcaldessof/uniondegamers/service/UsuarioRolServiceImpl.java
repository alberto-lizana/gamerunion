package com.segcaldessof.uniondegamers.service;

import static com.segcaldessof.uniondegamers.config.ErroresConstantes.NO_SE_HAN_ENCONTRADO_ASIGNACIONES;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.ROL_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.USUARIO_NO_ENCONTRADO;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.AsignarRolRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;
import com.segcaldessof.uniondegamers.exception.AsignacionesNoEncontradasException;
import com.segcaldessof.uniondegamers.exception.RolNoEncontradoException;
import com.segcaldessof.uniondegamers.exception.UsuarioNoEncontradoException;
import com.segcaldessof.uniondegamers.mappers.UsuarioMapper;
import com.segcaldessof.uniondegamers.model.Rol;
import com.segcaldessof.uniondegamers.model.Usuario;
import com.segcaldessof.uniondegamers.model.UsuarioRol;
import com.segcaldessof.uniondegamers.repository.RolRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRolRepository;

@Service 
public class UsuarioRolServiceImpl implements UsuarioRolService {

    private final UsuarioRolRepository usuarioRolRepository;
    private final RolRepository rolRepository;
    private final UsuarioRepository usuarioRepository;

    public UsuarioRolServiceImpl(
        UsuarioRolRepository usuarioRolRepository, 
        RolRepository rolRepository,
        UsuarioRepository usuarioRepository
    ){
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolRepository = rolRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Quita todos los roles de un usuario (sin borrar al usuario)
    @Transactional 
    @Override
    public void quitarTodosLosRoles(Long usuarioId) {
        if(usuarioRolRepository.borrarAsignacionesPorUsuarioId(usuarioId) == 0){
            throw new AsignacionesNoEncontradasException(NO_SE_HAN_ENCONTRADO_ASIGNACIONES);
        }
    }

    // Quita un rol específico a un usuario específico
    @Transactional 
    @Override
    public void quitarRol(Long usuarioId, Long rolId) {
        if(usuarioRolRepository.borrarAsignacion(usuarioId, rolId) == 0){
            throw new AsignacionesNoEncontradasException(NO_SE_HAN_ENCONTRADO_ASIGNACIONES);
        }
    }

    // Desasigna un rol de todos los usuarios (sin borrar el rol)
    @Transactional 
    @Override
    public void desasignarRolDeTodosLosUsuarios(Long rolId) {
        if(usuarioRolRepository.borrarAsignacionesPorRolId(rolId) == 0){
            throw new AsignacionesNoEncontradasException(NO_SE_HAN_ENCONTRADO_ASIGNACIONES);
        }
    }

    @Transactional 
    @Override
    public UsuarioResponseDto asignarRol(AsignarRolRequestDto dto) {
        Usuario us = usuarioRepository.findByIdUsuario(dto.idUsuario())
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO));
        
        Rol rol = rolRepository.findRolByNombre("ROLE_" + dto.nombreRol().trim().toUpperCase())
            .orElseThrow(() -> new RolNoEncontradoException(ROL_NO_ENCONTRADO));
        
        UsuarioRol usuarioRol = UsuarioRol.builder()
            .usuario(us)
            .rol(rol)
            .build();

        us.getAsignacionesRoles().add(usuarioRol);
        
        usuarioRolRepository.save(usuarioRol);

        return UsuarioMapper.toDto(us);
    }

}
