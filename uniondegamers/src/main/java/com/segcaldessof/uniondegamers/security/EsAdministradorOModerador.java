package com.segcaldessof.uniondegamers.security;

import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;

@Component
public class EsAdministradorOModerador {

    public boolean comprobar(Authentication authentication) {

        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
            .anyMatch(authority ->
                authority.getAuthority().equals("ROLE_ADMINISTRADOR") ||
                authority.getAuthority().equals("ROLE_MODERADOR")
            );
    }
}