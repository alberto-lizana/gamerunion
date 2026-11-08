package com.segcaldessof.uniondegamers.service;

import static com.segcaldessof.uniondegamers.config.ErroresConstantes.ROL_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.USUARIO_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.CREDENCIALES_EXISTENTES;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.UsuarioRequestDto;
import com.segcaldessof.uniondegamers.dto.UsuarioResponseDto;
import com.segcaldessof.uniondegamers.enums.RolEnum;
import com.segcaldessof.uniondegamers.exception.CredencialesExistentesException;
import com.segcaldessof.uniondegamers.exception.RolNoEncontradoException;
import com.segcaldessof.uniondegamers.exception.UsuarioNoEncontradoException;
import com.segcaldessof.uniondegamers.mappers.UsuarioMapper;
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
        return usuarioRepository.findAllConRoles()
            .stream()
            .map(UsuarioMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto obtenerPorId(Long id) {
        return UsuarioMapper.toDto(usuarioRepository.findByIdUsuario(id)
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO)));
    }

    @Transactional
    @Override
    public UsuarioResponseDto crear(UsuarioRequestDto dto) {

        if (usuarioRepository.existenCredenciales(dto.username(), dto.email())) {
            throw new CredencialesExistentesException(CREDENCIALES_EXISTENTES);
        }

        Rol rolDefecto = rolRepository.findRolByNombre(RolEnum.ROLE_USUARIO.name())
            .orElseThrow(() -> new RolNoEncontradoException(ROL_NO_ENCONTRADO));

        Usuario nuevoUsuario = Usuario
            .builder()
                .username(dto.username())
                .password(passwordConfig.passwordEncoder().encode(dto.password()))
                .nombre(dto.nombre())
                .appat(dto.appat())
                .email(dto.email())
                .asignacionesRoles(new ArrayList<>())
            .build();

        nuevoUsuario = usuarioRepository.save(nuevoUsuario);

        UsuarioRol asignacion = UsuarioRol
            .builder()
                .usuario(nuevoUsuario)
                .rol(rolDefecto)
            .build();

        usuarioRolRepository.save(asignacion);

        nuevoUsuario.getAsignacionesRoles().add(asignacion);

        return UsuarioMapper.toDto(nuevoUsuario);
    }


    @Transactional 
    @Override
    public UsuarioResponseDto borrarLogico(Long id) {
        Usuario response = usuarioRepository.findByIdUsuario(id)
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO));
        
        response.setIsActive(false);
        
        return UsuarioMapper.toDto(response);
    }

    @Transactional
    @Override
    public void borrarPorId(Long id) {
        if (usuarioRepository.deleteByIdUsuario(id) == 0) {
            throw new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public UsuarioResponseDto obtenerPerfil(String username) {
        return UsuarioMapper.toDto(usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO)));
    }
}
