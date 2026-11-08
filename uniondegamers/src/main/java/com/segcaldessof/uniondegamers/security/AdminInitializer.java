package com.segcaldessof.uniondegamers.security;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.segcaldessof.uniondegamers.config.AdminProperties;
import com.segcaldessof.uniondegamers.enums.RegionEnum;
import com.segcaldessof.uniondegamers.enums.RolEnum;
import com.segcaldessof.uniondegamers.model.Evento;
import com.segcaldessof.uniondegamers.model.Rol;
import com.segcaldessof.uniondegamers.model.Usuario;
import com.segcaldessof.uniondegamers.model.UsuarioRol;
import com.segcaldessof.uniondegamers.repository.EventoRepository;
import com.segcaldessof.uniondegamers.repository.RolRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRepository;
import com.segcaldessof.uniondegamers.repository.UsuarioRolRepository;

@Component
public class AdminInitializer implements ApplicationRunner {

    private final UsuarioRepository usuarioRepository;
    private final RolRepository rolRepository;
    private final AdminProperties adminProperties;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioRolRepository usuarioRolRepository;
    private final EventoRepository eventoRepository;


    public AdminInitializer(
        UsuarioRepository usuarioRepository,
        AdminProperties adminProperties,
        PasswordEncoder passwordEncoder,
        RolRepository rolRepository,
        UsuarioRolRepository usuarioRolRepository,
        EventoRepository eventoRepository
    ){
        this.usuarioRepository = usuarioRepository;
        this.adminProperties = adminProperties;
        this.passwordEncoder = passwordEncoder;
        this.rolRepository = rolRepository;
        this.usuarioRolRepository = usuarioRolRepository;
        this.eventoRepository = eventoRepository;
    }

    @Transactional 
    @Override
    public void run(ApplicationArguments args) throws Exception {

        Map<String, Rol> rolesPorNombre = rolRepository.obtenerTodosLosRoles()
            .stream()
            .collect(Collectors.toMap(Rol::getNombre, Function.identity()));

        Rol rolAdministrador = rolesPorNombre.get(RolEnum.ROLE_ADMINISTRADOR.name());

        Rol rolModerador = rolesPorNombre.get(RolEnum.ROLE_MODERADOR.name());

        if (rolAdministrador == null || rolModerador == null) {
            throw new IllegalStateException("No se encontraron todos los roles necesarios");
        }

        if (usuarioRepository.existsByEmail(adminProperties.email())) {
            return;
        }

        Usuario admin = Usuario
            .builder()
                .username(adminProperties.username())
                .password(passwordEncoder.encode(adminProperties.password()))
                .nombre(adminProperties.nombre())
                .appat(adminProperties.appat())
                .email(adminProperties.email())
                .isActive(true)
            .build();

        usuarioRepository.save(admin);

        UsuarioRol asignacionAdmin = UsuarioRol
            .builder()
                .rol(rolAdministrador)
                .usuario(admin)
            .build();

        UsuarioRol asignacionModerador = UsuarioRol
            .builder()
                .rol(rolModerador)
                .usuario(admin)
            .build();

        usuarioRolRepository.save(asignacionAdmin);
        usuarioRolRepository.save(asignacionModerador);

        admin.setAsignacionesRoles(List.of(asignacionAdmin, asignacionModerador));

        if (eventoRepository.count() == 0) {
            eventoRepository.saveAll(List.of(
                Evento
                    .builder()
                        .titulo("Torneo de FIFA")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2026, 12, 5, 18, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2026, 12, 5, 23, 0))
                        .ubicacion("Gimnasio Municipal, Rancagua")
                        .region(RegionEnum.LIBERTADOR_BERNARDO_O1HIGGINS)
                    .build(),
                Evento
                    .builder()
                        .titulo("Noche de Smash Bros")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2026, 12, 12, 20, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2026, 12, 13, 1, 0))
                        .ubicacion("Club Gamer Centro, Santiago")
                        .region(RegionEnum.METROPOLITANA)
                    .build(),
                Evento
                    .builder()
                        .titulo("LAN Party de Verano")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2026, 12, 19, 15, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2026, 12, 20, 10, 0))
                        .ubicacion("Centro de Eventos, Puerto Montt")
                        .region(RegionEnum.LOS_LAGOS)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Valorant")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 1, 9, 16, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 1, 9, 22, 0))
                        .ubicacion("Gamer Arena, La Serena")
                        .region(RegionEnum.COQUIMBO)
                    .build(),
                Evento.
                    builder()
                        .titulo("Maratón de Mario Kart")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 1, 16, 14, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 1, 16, 20, 30))
                        .ubicacion("Casa de la Cultura, Talca")
                        .region(RegionEnum.MAULE)
                    .build(),
                Evento
                    .builder()
                        .titulo("Encuentro Retro Gaming")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 1, 23, 12, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 1, 23, 19, 0))
                        .ubicacion("Centro Cívico, Antofagasta")
                        .region(RegionEnum.ANTOFAGASTA)
                        .build(),
                Evento
                    .builder()
                        .titulo("Torneo de League of Legends")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 1, 30, 16, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 1, 30, 23, 0))
                        .ubicacion("Centro de Eventos, Concepción")
                        .region(RegionEnum.BIOBÍO)
                    .build(),
                Evento
                    .builder()
                        .titulo("Campeonato de Counter-Strike 2")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 2, 6, 15, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 2, 6, 22, 0))
                        .ubicacion("Arena Gaming, Rancagua")
                        .region(RegionEnum.LIBERTADOR_BERNARDO_O1HIGGINS)
                    .build(),
                Evento
                    .builder()
                        .titulo("Feria de Videojuegos Indie")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 2, 13, 11, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 2, 13, 19, 0))
                        .ubicacion("Centro Cultural, Valparaíso")
                        .region(RegionEnum.VALPARAÍSO)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Tekken 8")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 2, 20, 17, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 2, 20, 22, 0))
                        .ubicacion("Club Gamer, Santiago")
                        .region(RegionEnum.METROPOLITANA)
                    .build(),
                Evento
                    .builder()
                        .titulo("Noche de Minecraft")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 2, 27, 18, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 2, 28, 1, 0))
                        .ubicacion("Centro Juvenil, Chillán")
                        .region(RegionEnum.ÑUBLE)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Rocket League")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 3, 6, 15, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 3, 6, 21, 0))
                        .ubicacion("Gimnasio Municipal, Temuco")
                        .region(RegionEnum.ARAUCANÍA)
                    .build(),
                Evento
                    .builder()
                        .titulo("Expo Retro Consolas")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 3, 13, 10, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 3, 13, 18, 0))
                        .ubicacion("Centro Cultural, Valdivia")
                        .region(RegionEnum.LOS_RÍOS)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Street Fighter 6")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 3, 20, 16, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 3, 20, 22, 0))
                        .ubicacion("Arena Gamer, Viña del Mar")
                        .region(RegionEnum.VALPARAÍSO)
                    .build(),
                Evento
                    .builder()
                        .titulo("Competencia de Just Dance")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 3, 27, 15, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 3, 27, 20, 0))
                        .ubicacion("Gimnasio Municipal, Osorno")
                        .region(RegionEnum.LOS_LAGOS)
                        .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Pokémon")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 4, 3, 12, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 4, 3, 19, 0))
                        .ubicacion("Centro de Convenciones, Santiago")
                        .region(RegionEnum.METROPOLITANA)
                    .build(),
                Evento
                    .builder()
                        .titulo("LAN Party: Edición Competitiva")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 4, 10, 16, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 4, 11, 9, 0))
                        .ubicacion("Centro de Eventos, Concepción")
                        .region(RegionEnum.BIOBÍO)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Fortnite")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 4, 17, 15, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 4, 17, 22, 0))
                        .ubicacion("Gamer House, Iquique")
                        .region(RegionEnum.TARAPACÁ)
                    .build(),
                Evento
                    .builder()
                        .titulo("Encuentro de Juegos de Mesa")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 4, 24, 11, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 4, 24, 18, 0))
                        .ubicacion("Biblioteca Regional, Copiapó")
                        .region(RegionEnum.ATACAMA)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Overwatch 2")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 5, 1, 16, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 5, 1, 22, 0))
                        .ubicacion("Centro Gamer, Punta Arenas")
                        .region(RegionEnum.MAGALLANES)
                    .build(),
                Evento
                    .builder()
                        .titulo("Festival de Gaming Familiar")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 5, 8, 10, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 5, 8, 19, 0))
                        .ubicacion("Polideportivo Municipal, Arica")
                        .region(RegionEnum.ARICA_Y_PARINACOTA)
                    .build(),
                Evento
                    .builder()
                        .titulo("Torneo de Apex Legends")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 5, 15, 16, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 5, 15, 23, 0))
                        .ubicacion("Arena Gaming, Antofagasta")
                        .region(RegionEnum.ANTOFAGASTA)
                    .build(),
                Evento
                    .builder()
                        .titulo("Noche de Juegos Cooperativos")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 5, 22, 18, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 5, 22, 23, 0))
                        .ubicacion("Centro Cultural, Coyhaique")
                        .region(RegionEnum.AYSÉN)
                    .build(),
                Evento
                    .builder()
                        .titulo("Campeonato de FIFA")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 5, 29, 15, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 5, 29, 21, 0))
                        .ubicacion("Estadio Municipal, Curicó")
                        .region(RegionEnum.MAULE)
                    .build(),
                Evento
                    .builder()
                        .titulo("Festival de Videojuegos y Tecnología")
                        .autor(admin)
                        .fechaInicioEvento(LocalDateTime.of(2027, 6, 5, 10, 0))
                        .fechaTerminoEvento(LocalDateTime.of(2027, 6, 5, 20, 0))
                        .ubicacion("Centro de Eventos, Puerto Montt")
                        .region(RegionEnum.LOS_LAGOS)
                    .build()
            ));
        }
        
    }

}
