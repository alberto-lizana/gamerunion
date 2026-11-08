package com.segcaldessof.uniondegamers.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeViewController {

    // PermitAll
    @GetMapping("/")
    public String home() {
        return "home";
  
    }
}
