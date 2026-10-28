package com.segcaldessof.uniondegamers.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .authorizeHttpRequests(auth -> auth
                /*
                // USUARIO
                .requestMatchers("/usuario/obtener")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/usuario/obtener/{id}")
                    .permitAll()

                .requestMatchers("/usuario/crear")
                    .permitAll()

                .requestMatchers("/usuario/borrar-logico/{id}")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/usuario/borrar/{id}")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())
        
                // ROLES
                .requestMatchers("/rol/{id}")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/rol/nombre")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/rol/modificar")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/rol/borrar/{id}")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())
        
                // USUARIO_ROLES
                .requestMatchers("/usuario-rol/{usuarioId}/roles")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/usuario-rol/roles/{usuarioId}")  
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .requestMatchers("/usuario-rol/roles/{rolId}/usuarios")
                    .hasRole(RolEnum.ROLE_ADMINISTRADOR.nombreSinPrefijo())

                .anyRequest().authenticated()
                */

                .requestMatchers("/usuario/**").permitAll()
                .requestMatchers("/rol/**").permitAll()
                .requestMatchers("/usuario-rol/**").permitAll()
                .anyRequest().permitAll()   // solo para desarrollo
            );

            /*
            .authenticationProvider(authenticationProvider())
            .addFilterBefore(
                jwtAuthFilter,
                UsernamePasswordAuthenticationFilter.class
            );
            */

        return http.build();
    }

}
