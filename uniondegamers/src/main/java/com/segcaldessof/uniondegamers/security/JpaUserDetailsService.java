package com.segcaldessof.uniondegamers.security;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.model.Usuario;
import com.segcaldessof.uniondegamers.repository.UsuarioRepository;

@Service 
public class JpaUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public JpaUserDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Vemos si existe el usuario vía su username
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException(
                String.format( "username %s no existe en el sistema", username)
            ));

        // Convertimos los roles nuestros al formato o contexto de autoridades que Spring Security entiende
        List<GrantedAuthority> authorities =  usuario.getAsignacionesRoles()
            .stream()
            .map(r -> new SimpleGrantedAuthority(r.getRol().getNombre()))
            .collect(Collectors.toList());

        // Instanciamos la clase User de SpringSecurity y la creamos con los datos de nuestro usuario 
        return new User(
            usuario.getUsername(), 
            usuario.getPassword(), 
            usuario.getIsActive(), 
            true, 
            true, 
            true,  
            authorities
        );
    }
}

/*
    En el contexto de SpringSecurity el User se ve de la siguiente manera:
        new User(
            username,
            password,
            enabled,
            accountNonExpired,
            credentialsNonExpired,
            accountNonLocked,
            authorities
        );


    1.- enabled: 
            Si el usuario está habilitado para autenticarse.
    2.- accountNonExpired: 
            Si la cuenta todavía es válida.
    3.- credentialsNonExpired:
            Si las credenciales (contraseña) no han expirado.
    4.- accountNonLocked:
        	Si la cuenta no está bloqueada.
*/