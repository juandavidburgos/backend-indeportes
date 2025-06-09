package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models;


import java.util.List;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.EstadoMonitor;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.Rol;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_usuario;

    @Column(nullable = false)
    private String nombre;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String contrasena;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Rol rol;

    // Esta columna no está en SQL. Solo incluir si la agregas:
    @Column(name = "estado_monitor", nullable = false)
    @Enumerated(EnumType.STRING)
    private EstadoMonitor estadoMonitor = EstadoMonitor.activo;

    @ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
    private List<Evento> eventos;
}
