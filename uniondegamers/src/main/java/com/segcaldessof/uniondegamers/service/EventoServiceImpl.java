package com.segcaldessof.uniondegamers.service;

import static com.segcaldessof.uniondegamers.config.ErroresConstantes.EVENTO_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.USUARIO_NO_ENCONTRADO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.EventoCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoResponseDto;
import com.segcaldessof.uniondegamers.dto.RegionesResponseDto;
import com.segcaldessof.uniondegamers.enums.RegionEnum;
import com.segcaldessof.uniondegamers.exception.EventoNoEncontradoException;
import com.segcaldessof.uniondegamers.exception.UsuarioNoEncontradoException;
import com.segcaldessof.uniondegamers.mappers.EventoMapper;
import com.segcaldessof.uniondegamers.model.Evento;
import com.segcaldessof.uniondegamers.model.Usuario;
import com.segcaldessof.uniondegamers.repository.ComentarioRepository;
import com.segcaldessof.uniondegamers.repository.EventoRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRepository;

@Service 
public class EventoServiceImpl implements EventoService {

    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public EventoServiceImpl(
        EventoRepository eventoRepository,
        UsuarioRepository usuarioRepository,
        ComentarioRepository comentarioRepository
    ){
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    @Transactional(readOnly = true)
    @Override
    public List<RegionesResponseDto> obtenerRegiones() {
        return Arrays.stream(RegionEnum.values())
            .map(r -> new RegionesResponseDto(r.name(), r.nombreSinGuiones()))
            .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EventoResponseDto> obtenerTodos(Pageable pageable) {
        return eventoRepository.obtenerTodos(pageable).map(EventoMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<EventoResponseDto> obtenerTodosActivos(Pageable pageable) {
        return eventoRepository.obtenerTodosActivos(pageable).map(EventoMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public EventoResponseDto obtenerPorId(Long id) {
        return EventoMapper.toDto(eventoRepository.obtenerPorId(id)
            .orElseThrow(() -> new EventoNoEncontradoException(EVENTO_NO_ENCONTRADO)));
    }

    @Transactional(readOnly = true)
    @Override
    public EventoResponseDto obtenerActivoPorId(Long id) {
        return EventoMapper.toDto(eventoRepository.obtenerActivoPorId(id)
            .orElseThrow(() -> new EventoNoEncontradoException(EVENTO_NO_ENCONTRADO)));
    }


    @Transactional
    @Override
    public EventoResponseDto crear(EventoCrearRequestDto dto, Authentication authentication) {

        Usuario autor = usuarioRepository.findByUsername(authentication.getName())
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO));

        Evento ev = Evento
            .builder()
                .titulo(dto.titulo())
                .autor(autor)
                .fechaInicioEvento(dto.fechaInicioEvento())
                .fechaTerminoEvento(dto.fechaTerminoEvento())
                .ubicacion(dto.ubicacion())
                .region(dto.region())
            .build();
        
        eventoRepository.save(ev);

        if (autor.getEventosEscritos() == null) {
            autor.setEventosEscritos(new ArrayList<>());
        }

        autor.getEventosEscritos().add(ev);

        return EventoMapper.toDto(ev);

    }

    @Transactional
    @Override
    public EventoResponseDto modificar(EventoModificarRequestDto dto) {

        Evento ev = eventoRepository.obtenerPorId(dto.eventoId())
            .orElseThrow(() -> new EventoNoEncontradoException(EVENTO_NO_ENCONTRADO));

        if (!Objects.equals(dto.titulo(), ev.getTitulo())) {
            ev.setTitulo(dto.titulo());
        }

        if (!Objects.equals(dto.fechaInicioEvento(), ev.getFechaInicioEvento())) {
            ev.setFechaInicioEvento(dto.fechaInicioEvento());
        }

        if (!Objects.equals(dto.fechaTerminoEvento(), ev.getFechaTerminoEvento())) {
            ev.setFechaTerminoEvento(dto.fechaTerminoEvento());
        }

        if (!Objects.equals(dto.isActive(), ev.isActive())) {
            ev.setActive(dto.isActive());
        }

        if (!Objects.equals(dto.ubicacion(), ev.getUbicacion())) {
            ev.setUbicacion(dto.ubicacion());
        }

        if (!Objects.equals(dto.region(), ev.getRegion())) {
            ev.setRegion(dto.region());
        }

        return EventoMapper.toDto(ev);
    }

    @Transactional
    @Override
    public void borrarLogico(Long id) {
        Evento evento = eventoRepository.obtenerPorId(id)
            .orElseThrow(() -> new EventoNoEncontradoException(EVENTO_NO_ENCONTRADO));

        evento.setActive(false);
    }
    
    @Transactional
    @Override
    public void borrarFisico(Long id) {
        Evento evento = eventoRepository.obtenerPorId(id)
            .orElseThrow(() -> new EventoNoEncontradoException(EVENTO_NO_ENCONTRADO));

        evento.getAutor().getEventosEscritos().remove(evento);

        eventoRepository.delete(evento);
    }

}
