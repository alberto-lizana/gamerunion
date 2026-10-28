package com.segcaldessof.uniondegamers.model;

import java.util.List;

import com.segcaldessof.uniondegamers.enums.RolEnum;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "usuarios")
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String alias; 

    @Column(nullable = false, length = 50)
    private String password;

    @Column(nullable = false, length = 50)
    private String nombre;

    @Column(nullable = false, length = 50)
    private String appat;

    @Column(unique = true, nullable = false, length = 250)
    private String email;

    @Column (nullable = false)
    private Boolean isActive;

    @OneToMany(mappedBy = "usuario")
    private List<UsuarioRol> asignacionesRoles; 

    @PrePersist
    public void prePersist() {
        isActive = true;
    }

    public boolean isAdmin() {
        return asignacionesRoles.stream()
            .anyMatch(ur -> ur.getRol().getNombre().equals(RolEnum.ROLE_ADMINISTRADOR.name()));
    }

}