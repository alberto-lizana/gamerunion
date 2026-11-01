package com.segcaldessof.uniondegamers.security;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.segcaldessof.uniondegamers.dto.ErrorResponseDto;
import com.segcaldessof.uniondegamers.dto.LoginRequestDto;
import com.segcaldessof.uniondegamers.dto.LoginResponseDto;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import tools.jackson.databind.ObjectMapper;

import static com.segcaldessof.uniondegamers.security.TokenJwtConfig.*;

/*
    JwtAuthenticationFilter: 
    Tiene como objetivo recibir las credenciales del usuario, extraerlas de la petición HTTP y entregárselas a Spring Security para que compruebe si son correctas.

    UsernamePasswordAuthenticationFilter: 
    Es un filtro que proporciona Spring Security para procesar autenticaciones mediante nombre de usuario y contraseña.

    AuthenticationManager:
    Es una interfaz de Spring Security que representa el componente encargado de gestionar el proceso de autenticación.

    ObjectMapper:
    Es una clase de la biblioteca Jackson que se utiliza para convertir datos JSON en objetos Java y viceversa.

    subject: El nombre de usuario.
    issuedAt: La fecha de creación del token.
    expiration: La fecha de expiración.
    authorities: Los permisos o autoridades del usuario.

*/
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public JwtAuthenticationFilter(
        AuthenticationManager authenticationManager, 
        ObjectMapper objectMapper,
        Validator validator
    ){
        this.authenticationManager = authenticationManager;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    public Authentication attemptAuthentication(
        HttpServletRequest request,
        HttpServletResponse response
    ) throws AuthenticationException {

        try {

            LoginRequestDto login = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);

            Set<ConstraintViolation<LoginRequestDto>> errores = validator.validate(login);

            if (!errores.isEmpty()) {
                throw new BadCredentialsException("Credenciales incompletas");
            }            

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(login.username(), login.password());

            return authenticationManager.authenticate(authenticationToken);

        } catch (IOException e) {
            throw new AuthenticationServiceException("Error al procesar las credenciales del usuario",e);
        }
    }

    @Override
    protected void successfulAuthentication(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain,
        Authentication authResult
    ) throws IOException, ServletException {

        UserDetails usuario = (UserDetails) authResult.getPrincipal();

        String token = Jwts.builder()
            .subject(usuario.getUsername())
            .claim("authorities",
                usuario.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList()
            )
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + 3600000))
            .signWith(SECRET_KEY)
            .compact();

        response.setHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        objectMapper.writeValue(response.getWriter(), LoginResponseDto
            .builder()
                .token(token)
                .username(usuario.getUsername())
                .mensaje(String.format("¡Hola %s has iniciado sesión con éxito!", usuario.getUsername()))
            .build());

    }

    @Override
    protected void unsuccessfulAuthentication(
        HttpServletRequest request, 
        HttpServletResponse response,
        AuthenticationException failed
    ) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(CONTENT_TYPE);
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDto error = ErrorResponseDto
            .builder()
                .mensaje("Error de autenticación")
                .errores(List.of("El usuario o la contraseña son incorrectos"))
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
            .build();

        objectMapper.writeValue(response.getWriter(), error);

    }

}
