package com.segcaldessof.uniondegamers.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.segcaldessof.uniondegamers.dto.UsuarioRequestDto;
import com.segcaldessof.uniondegamers.service.UsuarioService;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/usuarios")
public class UsuarioViewController {

    private final UsuarioService usuarioService;

    public UsuarioViewController(UsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    // ADMINITRADOR
    @GetMapping("/obtener")
    public String obtenerTodos(Model model) {
        model.addAttribute("usuarios", usuarioService.obtenerTodos());
        return "usuarios/lista";
  
    }

    // ADMINITRADOR
    @GetMapping("/obtener/{id}")
    public String obtenerPorId(@PathVariable Long id, Model model) {

        model.addAttribute("usuario", usuarioService.obtenerPorId(id));

        return "usuarios/detalle";

    }

    // Authenticated
    @GetMapping("/perfil")
    public String obtenerPerfil(Authentication authentication, Model model) {

        model.addAttribute("usuario", usuarioService.obtenerPerfil(authentication.getName()));

        return "usuarios/perfil";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {

        model.addAttribute("usuario", new UsuarioRequestDto(null, null, null, null, null));

        return "usuarios/crear";
    }
    
    // PermitAll
    @PostMapping("/crear")
    public String crear(@Valid @ModelAttribute UsuarioRequestDto dto, BindingResult result
    ) {

        if (result.hasErrors()) {
            return "usuarios/crear";
        }

        usuarioService.crear(dto);

        return "redirect:/login";
    }

    // Todavía no creada HTML 
    @PostMapping("/borrar-logico/{id}")
    public String borrarLogico(@PathVariable Long id) {

        usuarioService.borrarLogico(id);

        return "redirect:/usuarios/obtener";
    }

    // Todavía no creada HTML 
    @PostMapping("/borrar/{id}")
    public String borrarFisico(@PathVariable Long id) {

        usuarioService.borrarPorId(id);

        return "redirect:/usuarios/obtener";
    }

}
