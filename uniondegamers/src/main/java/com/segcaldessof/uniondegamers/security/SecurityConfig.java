package com.segcaldessof.uniondegamers.security;

import static com.segcaldessof.uniondegamers.config.ErroresConstantes.ADMINISTRADOR;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.MODERADOR;
import static com.segcaldessof.uniondegamers.config.ErroresConstantes.USUARIO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.validation.Validator;
import tools.jackson.databind.ObjectMapper;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfig(AuthenticationConfiguration authenticationConfiguration){
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean 
    AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChain(
        HttpSecurity http,
        AuthenticationManager authenticationManager,
        ObjectMapper objectMapper,
        Validator validator
    ) throws Exception {

        JwtAuthenticationFilter loginFilter = new JwtAuthenticationFilter(
            authenticationManager,
            objectMapper,
            validator
        );
        loginFilter.setFilterProcessesUrl("/api/auth/login");

        http
            .securityMatcher("/api/**")
            .csrf(csrf -> csrf.disable())
            .sessionManagement(sm ->
                sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )
            .addFilter(loginFilter)
            .addFilterBefore(
                new JwtValidationFilter(),
                UsernamePasswordAuthenticationFilter.class
            )
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/api/rol/**").hasRole(ADMINISTRADOR)
        
                .requestMatchers("/api/usuario/perfil").authenticated()
                .requestMatchers("/api/usuario/obtener/**").hasRole(ADMINISTRADOR)
                .requestMatchers("/api/usuario/crear").permitAll()

                .requestMatchers("/api/usuario/borrar-logico/{id}").hasAnyRole(ADMINISTRADOR, MODERADOR)
                .requestMatchers("/api/usuario/borrar/{id}").hasRole(ADMINISTRADOR)

                .requestMatchers("/api/usuario-rol/asignar-rol").hasRole(ADMINISTRADOR)


                .requestMatchers("/api/evento/**").permitAll()
                .requestMatchers("/api/comentario/**").permitAll()

                .anyRequest().authenticated()
            );

        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain webChain(HttpSecurity http) throws Exception {

        http
            .formLogin(f -> f
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
            )

            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self'; " +
                        "script-src 'self' https://cdn.jsdelivr.net; " +
                        "style-src 'self' https://cdn.jsdelivr.net; " +
                        "img-src 'self' data:; " +
                        "font-src 'self' data:; " +
                        "object-src 'none'; " +
                        "frame-ancestors 'none'; " +
                        "base-uri 'self'; " +
                        "form-action 'self'; " +
                        "frame-src 'none'; " +
                        "manifest-src 'self'; " +
                        "worker-src 'self';"
                    )
                )
            )
            .authorizeHttpRequests(a -> a

                .requestMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/login").permitAll()
                .requestMatchers("/usuarios/crear").permitAll()
                .requestMatchers("/eventos").permitAll()
                .requestMatchers("/eventos/{id:[0-9]+}").permitAll()
                .requestMatchers("/eventos/cargar").permitAll()

                .requestMatchers("/usuarios/perfil").authenticated()

                .requestMatchers("/usuarios/obtener").hasRole(ADMINISTRADOR)
                .requestMatchers("/usuarios/obtener/{id:[0-9]+}").hasRole(ADMINISTRADOR)
                .requestMatchers("/usuarios/borrar-logico/{id:[0-9]+}").hasRole(ADMINISTRADOR)
                .requestMatchers("/usuarios/borrar/{id:[0-9]+}").hasRole(ADMINISTRADOR)

                .requestMatchers(HttpMethod.GET, "/eventos/crear").hasAnyRole(ADMINISTRADOR, MODERADOR)
                .requestMatchers(HttpMethod.POST, "/eventos/crear").hasAnyRole(ADMINISTRADOR, MODERADOR)
                
                .requestMatchers(HttpMethod.GET, "/eventos/modificar").hasAnyRole(ADMINISTRADOR, MODERADOR)
                .requestMatchers(HttpMethod.POST, "/eventos/modificar").hasAnyRole(ADMINISTRADOR, MODERADOR)

                .requestMatchers(HttpMethod.POST, "/eventos/eliminar-logico-{id}").hasAnyRole("ADMINISTRADOR", "MODERADOR")
                .requestMatchers(HttpMethod.POST, "/eventos/eliminar-fisico-{id}").hasRole("ADMINISTRADOR")
                

                .anyRequest().authenticated()
            )

            .logout(l -> l
                .logoutSuccessUrl("/")
            );

        return http.build();
    }
}

/*
Esta configuración establece que:

    default-src 'self':	
        Por defecto, permite recursos del mismo origen.
    script-src 'self':
        Solo permite scripts del mismo origen.
    style-src 'self':
        Solo permite CSS del mismo origen.
    img-src 'self' data:
        Permite imágenes locales y data URLs.
    font-src 'self':
        Permite fuentes del mismo origen.
    object-src 'none':
        Bloquea objetos y plugins.
    frame-ancestors 'none':
        Impide que otros sitios incrusten tu aplicación.
    base-uri 'self':
        Restringe las etiquetas <base>.

*/