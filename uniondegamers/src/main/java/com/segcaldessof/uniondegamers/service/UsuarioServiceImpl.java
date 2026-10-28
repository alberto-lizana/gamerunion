package com.segcaldessof.uniondegamers.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.RolResponseDto;
import com.segcaldessof.uniondegamers.dto.UsuarioRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;
import com.segcaldessof.uniondegamers.dto.UsuarioRolResponseDto;
import com.segcaldessof.uniondegamers.enums.RolEnum;
import com.segcaldessof.uniondegamers.exception.RolNoEncontradoException;
import com.segcaldessof.uniondegamers.exception.UsuarioNoEncontradoException;
import com.segcaldessof.uniondegamers.model.Rol;
import com.segcaldessof.uniondegamers.model.Usuario;
import com.segcaldessof.uniondegamers.model.UsuarioRol;
import com.segcaldessof.uniondegamers.repository.RolRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRolRepository;
import com.segcaldessof.uniondegamers.security.PasswordConfig;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final UsuarioRolRepository usuarioRolRepository;
    private final PasswordConfig passwordConfig;

    public UsuarioServiceImpl(
        UsuarioRepository usuarioRepository,
        RolRepository rolRepository,
        UsuarioRolRepository usuarioRolRepository,
        PasswordConfig passwordConfig
    ){
        this.usuarioRepository = usuarioRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.rolRepository = rolRepository;
        this.passwordConfig = passwordConfig;
    }

    @Transactional(readOnly = true)
    @Override
    public List<UsuarioResponseDto> obtenerTodos() {
        return usuarioRepository.findAllConRoles().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto obtenerPorId(Long id) {
        return toDto(usuarioRepository.findByIdUsuario(id)
            .orElseThrow(() -> new UsuarioNoEncontradoException("No se ha encontrado al usuario")));
    }

    @Transactional
    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto dto) {

        Rol rolDefecto = rolRepository.findRolByNombre(RolEnum.ROLE_USUARIO.name())
            .orElseThrow(() -> new RolNoEncontradoException("No se encontró el rol"));

        Usuario nuevoUsuario = Usuario
            .builder()
                .alias(dto.alias())
                .password(passwordConfig.passwordEncoder().encode(dto.password()))
                .nombre(dto.nombre())
                .appat(dto.appat())
                .email(dto.email())
            .build();

        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        UsuarioRol asignacion = UsuarioRol
            .builder()
                .usuario(nuevoUsuario)
                .rol(rolDefecto)
            .build();
        
        usuarioRolRepository.save(asignacion);

        return toDto(nuevoUsuario);
    }


    @Transactional 
    @Override
    public UsuarioResponseDto borrarLogico(Long id) {
        Usuario response = usuarioRepository.findByIdUsuario(id)
            .orElseThrow(() -> new UsuarioNoEncontradoException("No se ha encontrado al usuario"));
        
        response.setIsActive(false);
        
        return toDto(response);
    }

    @Transactional
    @Override
    public void borrarPorId(Long id) {
        if (usuarioRepository.deleteByIdUsuario(id) == 0) {
            throw new UsuarioNoEncontradoException("No se ha encontrado al usuario con el id %s".formatted(id));
        }
    }

    private UsuarioResponseDto toDto(Usuario usuario){
        return UsuarioResponseDto
            .builder()
                .id(usuario.getId())
                .alias(usuario.getAlias())
                .nombre(usuario.getNombre())
                .appat(usuario.getAppat())
                .email(usuario.getEmail())
                .isActive(usuario.getIsActive())
                .asignacionesRoles(usuario.getAsignacionesRoles()
                    .stream()
                        .map(this::toDtoUsuarioRol)
                    .toList())
            .build();
    }

    private RolResponseDto toDtoRolResponseDto(Rol rol){
        return RolResponseDto
            .builder()
                .idRol(rol.getId())
                .nombre(rol.getNombre())
            .build();
    }

    private UsuarioRolResponseDto toDtoUsuarioRol(UsuarioRol usuarioRol){
        return UsuarioRolResponseDto
            .builder()
                .rol(toDtoRolResponseDto(usuarioRol.getRol()))
            .build();
    }
}
