package com.segcaldessof.uniondegamers.security;

import static com.segcaldessof.uniondegamers.security.TokenJwtConfig.HEADER_AUTHORIZATION;
import static com.segcaldessof.uniondegamers.security.TokenJwtConfig.PREFIX_TOKEN;
import static com.segcaldessof.uniondegamers.security.TokenJwtConfig.SECRET_KEY;
import static com.segcaldessof.uniondegamers.security.TokenJwtConfig.CONTENT_TYPE;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.segcaldessof.uniondegamers.dto.ErrorResponseDto;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

public class JwtValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain chain
    ) throws IOException, ServletException {

        String header = request.getHeader(HEADER_AUTHORIZATION);

        if (header == null || !header.startsWith(PREFIX_TOKEN)) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.substring(PREFIX_TOKEN.length());

        try {

            Claims claims = Jwts.parser()
                .verifyWith(SECRET_KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

            Object authoritiesClaim = claims.get("authorities");

            if (!(authoritiesClaim instanceof List<?> roles) || !roles.stream().allMatch(String.class::isInstance)) {
                throw new JwtException("El token no contiene authorities");
            }

            List<GrantedAuthority> authorities = roles.stream()
                .map(String.class::cast)
                .map(role -> (GrantedAuthority) new SimpleGrantedAuthority(role))
                .toList();

            UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(claims.getSubject(), null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        } catch (JwtException | IllegalArgumentException e) {

            SecurityContextHolder.clearContext();

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType(CONTENT_TYPE);
            response.setCharacterEncoding("UTF-8");

            ErrorResponseDto error = ErrorResponseDto.builder()
                .mensaje("El token JWT no es válido o ha expirado")
                .status(HttpServletResponse.SC_UNAUTHORIZED)
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

            new ObjectMapper().writeValue(response.getWriter(), error);

            return;
        }

        chain.doFilter(request, response);
    }
}
