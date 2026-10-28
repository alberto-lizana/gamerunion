package com.segcaldessof.uniondegamers.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.RolNombreRequestDto;
import com.segcaldessof.uniondegamers.dto.RolResponseDto;
import com.segcaldessof.uniondegamers.exception.RolNoEncontradoException;
import com.segcaldessof.uniondegamers.model.Rol;
import com.segcaldessof.uniondegamers.repository.RolRepository;

@Service 
public class RolServiceImpl implements RolService {

    private final RolRepository rolRepository;

    public RolServiceImpl(RolRepository rolRepository){
        this.rolRepository = rolRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public List<RolResponseDto> obtenerTodos() {
        List<Rol> roles = rolRepository.findAll(); 

        return roles.stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public RolResponseDto obtenerPorId(Long id) {
        Rol rol = rolRepository.findById(id)
            .orElseThrow(() -> new RolNoEncontradoException("Rol no encontrado")); 

        return toDto(rol);
    }

    @Transactional
    @Override
    public RolResponseDto modificarNombre(RolNombreRequestDto dto) {
        Rol rol = rolRepository.findById(dto.idRol())
            .orElseThrow(() -> new RolNoEncontradoException("Rol no encontrado"));

        rol.setNombre(dto.nombre());

        return toDto(rol);
    }

    private RolResponseDto toDto(Rol rol) {
        return RolResponseDto
            .builder()
                .idRol(rol.getId())
                .nombre(rol.getNombre())
            .build();
    }

    @Override
    public void eliminar(Long id) {
        if(rolRepository.deleteByIdRol(id) == 0){
            throw new RolNoEncontradoException("Rol no encontrado");
        }
    }
}


