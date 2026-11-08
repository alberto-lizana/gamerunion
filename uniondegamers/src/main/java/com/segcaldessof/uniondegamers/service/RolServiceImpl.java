package com.segcaldessof.uniondegamers.service;

import static com.segcaldessof.uniondegamers.config.ErroresConstantes.ROL_NO_ENCONTRADO;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.RolNombreRequestDto;
import com.segcaldessof.uniondegamers.dto.RolResponseDto;
import com.segcaldessof.uniondegamers.exception.RolNoEncontradoException;
import com.segcaldessof.uniondegamers.mappers.RolMapper;
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
        return rolRepository.findAll()
            .stream()
            .map(RolMapper::toDto)
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public RolResponseDto obtenerPorId(Long id) {
        return RolMapper.toDto(rolRepository.findById(id)
            .orElseThrow(() -> new RolNoEncontradoException(ROL_NO_ENCONTRADO))
        );
    }

    @Transactional
    @Override
    public RolResponseDto modificarNombre(RolNombreRequestDto dto) {
        Rol rol = rolRepository.findRolById(dto.idRol())
            .orElseThrow(() -> new RolNoEncontradoException(ROL_NO_ENCONTRADO));

        rol.setNombre(dto.nombre());

        return RolMapper.toDto(rol);
    }


    @Transactional 
    @Override
    public void eliminar(Long id) {
        if(rolRepository.deleteByIdRol(id) == 0){
            throw new RolNoEncontradoException(ROL_NO_ENCONTRADO);
        }
    }
}


