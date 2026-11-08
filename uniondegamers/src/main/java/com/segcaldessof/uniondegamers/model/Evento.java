package com.segcaldessof.uniondegamers.model;

import java.time.LocalDateTime;
import java.util.List;

import com.segcaldessof.uniondegamers.enums.RegionEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "eventos")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario autor;

    @Column(nullable = false)
    private LocalDateTime fechaInicioEvento;

    @Column(nullable = false)
    private LocalDateTime fechaTerminoEvento;

    @Column(nullable = false, length = 500)
    private String ubicacion;

    private boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RegionEnum region;

    @Column(updatable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaModificacion;

    @OneToMany(
        mappedBy = "evento",
        cascade = CascadeType.REMOVE
    )
    private List<Comentario> comentarios;
    
    @PrePersist
    public void prePersist() {
        fechaCreacion = LocalDateTime.now();
        fechaModificacion = fechaCreacion;
        isActive = true;
    }

    @PreUpdate
    public void preUpdate() {
        fechaModificacion = LocalDateTime.now();
    }
    
}