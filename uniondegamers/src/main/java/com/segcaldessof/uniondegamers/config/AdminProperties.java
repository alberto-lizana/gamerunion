package com.segcaldessof.uniondegamers.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.admin")
public record AdminProperties(

    String username,
    String password,
    String nombre,
    String appat,
    String email
    
) {}