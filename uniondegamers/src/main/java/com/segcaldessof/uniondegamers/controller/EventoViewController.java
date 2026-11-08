package com.segcaldessof.uniondegamers.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.segcaldessof.uniondegamers.dto.EventoCrearRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoModificarRequestDto;
import com.segcaldessof.uniondegamers.dto.EventoResponseDto;
import com.segcaldessof.uniondegamers.security.EsAdministradorOModerador;
import com.segcaldessof.uniondegamers.service.ComentarioService;
import com.segcaldessof.uniondegamers.service.EventoService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/eventos")
public class EventoViewController {

    private final EventoService eventoService;
    private final ComentarioService comentarioService;
    private final EsAdministradorOModerador esAdministradorOModerador;

    public EventoViewController(
        EventoService eventoService,
        ComentarioService comentarioService,
        EsAdministradorOModerador esAdministradorOModerador
    ) {
        this.eventoService = eventoService;
        this.comentarioService = comentarioService;
        this.esAdministradorOModerador = esAdministradorOModerador;
    }

    @GetMapping
    public String listar(
        @PageableDefault(size = 12) Pageable pageable,
        Authentication authentication,
        Model model
    ) {

        var paginaEventos = esAdministradorOModerador.comprobar(authentication)
            ? eventoService.obtenerTodos(pageable)
            : eventoService.obtenerTodosActivos(pageable);

        model.addAttribute("eventos", paginaEventos.getContent());
        model.addAttribute("paginaEventos", paginaEventos);

        return "eventos/lista";
    }

    @GetMapping("/cargar")
    public String cargarMas(
        @PageableDefault(size = 12) Pageable pageable,
        Authentication authentication,
        Model model
    ) {

        var paginaEventos = esAdministradorOModerador.comprobar(authentication)
            ? eventoService.obtenerTodos(pageable)
            : eventoService.obtenerTodosActivos(pageable);

        model.addAttribute("eventos", paginaEventos.getContent());
        model.addAttribute("paginaEventos", paginaEventos);

        return "/fragments/lista-eventos";
    }

    @GetMapping("/{id}")
    public String detalle(
        @PathVariable Long id,
        @PageableDefault(size = 12) Pageable pageable,
        Authentication authentication,
        Model model
    ) {

        if (esAdministradorOModerador.comprobar(authentication)) {
            model.addAttribute("evento", eventoService.obtenerPorId(id));
        } else {
            model.addAttribute("evento", eventoService.obtenerActivoPorId(id));
        }

        model.addAttribute("comentarios", comentarioService.obtenerPorEventoId(id, pageable));

        return "eventos/detalle";
    }

    // Solo Administrador y moderador
    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {

        model.addAttribute("regiones", eventoService.obtenerRegiones());
        model.addAttribute("evento", new EventoCrearRequestDto(null, null, null, null, null));

        return "eventos/crear";
    }
    
    // Solo Administrador y moderador
    @PostMapping("/crear")
    public String crear(
        @Valid @ModelAttribute("evento") EventoCrearRequestDto dto,
        BindingResult result,
        Authentication authentication,
        Model model
    ) {

         String username = authentication.getPrincipal().toString();
         System.out.println(username);

        if (result.hasErrors()) {
            model.addAttribute("regiones", eventoService.obtenerRegiones());
            return "eventos/crear";
        }

        eventoService.crear(dto, authentication);

        return "redirect:/eventos";
    }

    // Solo Administrador y Moderador
    @GetMapping("/modificar")
    public String mostrarFormularioModificar(@RequestParam Long eventoId, Model model) {

        // Aquí buscamos el evento por su ID
        EventoResponseDto evento = eventoService.obtenerPorId(eventoId);

        // Empaquetamos sus datos en el DTO que utilizará el formulario
        EventoModificarRequestDto dto = new EventoModificarRequestDto(
            evento.id(),
            evento.titulo(),
            evento.fechaInicioEvento(),
            evento.fechaTerminoEvento(),
            evento.isActive(),
            evento.ubicacion(),
            evento.region()
        );

        model.addAttribute("evento", dto);
        model.addAttribute("regiones", eventoService.obtenerRegiones());

        return "eventos/modificar";
    }

    // Solo Administrador y Moderador
    @PostMapping("/modificar")
    public String modificar(
            @ModelAttribute("evento") @Valid EventoModificarRequestDto dto,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            model.addAttribute("regiones", eventoService.obtenerRegiones());
            return "eventos/modificar";
        }

        eventoService.modificar(dto);

        return "redirect:/eventos";
    }

    // Solo Administrador y Moderador
    @PostMapping("/eliminar-logico-{id}")
    public String eliminarLogico(@PathVariable Long id) {

        eventoService.borrarLogico(id);

        return "redirect:/eventos";
    }

    // Solo Administrador y Moderador
    @PostMapping("/eliminar-fisico-{id}")
    public String eliminarFisico(@PathVariable Long id) {

        eventoService.borrarFisico(id);

        return "redirect:/eventos";
    }

}