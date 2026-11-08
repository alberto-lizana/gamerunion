package com.segcaldessof.uniondegamers.service;

import static com.segcaldessof.uniondegamers.config.ErroresConstantes.COMENTARIO_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.EVENTO_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.USUARIO_NO_ENCONTRADO;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.ACCESO_NO_AUTORIZADO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.dto.ComentarioCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.ComentarioModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.ComentarioResponseDto;
import com.segcaldessof.uniondegamers.exception.AccesoNoAutorizadoException;
import com.segcaldessof.uniondegamers.exception.ComentarioNoEncntradoException;
import com.segcaldessof.uniondegamers.exception.EventoNoEncontradoException;
import com.segcaldessof.uniondegamers.exception.UsuarioNoEncontradoException;
import com.segcaldessof.uniondegamers.mappers.ComentarioMapper;
import com.segcaldessof.uniondegamers.model.Comentario;
import com.segcaldessof.uniondegamers.model.Evento;
import com.segcaldessof.uniondegamers.model.Usuario;
import com.segcaldessof.uniondegamers.repository.ComentarioRepository;
import com.segcaldessof.uniondegamers.repository.EventoRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRepository;

@Service 
public class ComentarioServiceImpl implements ComentarioService {

    private final ComentarioRepository comentarioRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    public ComentarioServiceImpl(
        ComentarioRepository comentarioRepository,
        EventoRepository eventoRepository,
        UsuarioRepository usuarioRepository
    ){
        this.comentarioRepository = comentarioRepository;
        this.eventoRepository = eventoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    
    @Transactional(readOnly = true)
    @Override
    public Page<ComentarioResponseDto> obtenerTodos(Pageable pageable) {
        return comentarioRepository.obtenerTodos(pageable).map(ComentarioMapper::toDto);
    }

    @Transactional(readOnly = true)
    @Override
    public ComentarioResponseDto obtenerPorId(Long id) {
        return ComentarioMapper.toDto(comentarioRepository.obtenerPorId(id)
            .orElseThrow(() -> new ComentarioNoEncntradoException(COMENTARIO_NO_ENCONTRADO)));
    }

    @Transactional(readOnly = true)
    @Override
    public Page<ComentarioResponseDto> obtenerPorEventoId(Long eventoId, Pageable pageable) {
        return comentarioRepository.obtenerTodosPorEventoId(eventoId, pageable).map(ComentarioMapper::toDto);
    }

    @Transactional
    @Override
    public ComentarioResponseDto crear(ComentarioCrearRequestDto dto, Authentication authentication) {
        
        Usuario us = usuarioRepository.findByUsername(authentication.getPrincipal().toString())
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO));
        
        Evento ev = eventoRepository.obtenerPorId(dto.eventoId())
            .orElseThrow(() -> new EventoNoEncontradoException(EVENTO_NO_ENCONTRADO));
        
        Comentario com = Comentario
            .builder()
                .contenido(dto.contenido())
                .escritor(us)
                .evento(ev)
            .build();

        Comentario guardado = comentarioRepository.save(com);

        us.getComentarios().add(guardado);
        ev.getComentarios().add(guardado);

        return ComentarioMapper.toDto(guardado);
    }

    @Transactional
    @Override
    public ComentarioResponseDto modificar(ComentarioModificarRequestDto dto, Authentication authentication) {

        Usuario us = usuarioRepository.findByUsername(authentication.getPrincipal().toString())
            .orElseThrow(() -> new UsuarioNoEncontradoException(USUARIO_NO_ENCONTRADO));

        Comentario comentario = comentarioRepository.obtenerPorId(dto.comentarioId())
            .orElseThrow(() -> new ComentarioNoEncntradoException(COMENTARIO_NO_ENCONTRADO));


        if (!comentario.getEscritor().getId().equals(us.getId())) {
            throw new AccesoNoAutorizadoException(ACCESO_NO_AUTORIZADO);
        }

        comentario.setContenido(dto.contenido());

        return ComentarioMapper.toDto(comentario);
    }

    @Transactional
    @Override
    public void eliminar(Long id) {
        Comentario comentario = comentarioRepository.obtenerPorId(id)
            .orElseThrow(() -> new ComentarioNoEncntradoException(COMENTARIO_NO_ENCONTRADO));

        comentario.getEscritor().getComentarios().remove(comentario);
        comentario.getEvento().getComentarios().remove(comentario);

        comentarioRepository.delete(comentario);
    }

}
