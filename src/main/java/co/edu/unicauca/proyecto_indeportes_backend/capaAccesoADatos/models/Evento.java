package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.EstadoEvento;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
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
@Table (name = "Eventos")
public class Evento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_evento;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String fecha_hora_inicio;

    @Column(nullable = false)
    private String fecha_hora_fin;

    @Column(nullable = false)
    private String ubicacion;

    @Column
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoEvento estado = EstadoEvento.activo;

    @ManyToMany
    @JoinTable(
        name = "evento_usuario",
        joinColumns = @JoinColumn(name = "id_evento"),
        inverseJoinColumns = @JoinColumn(name = "id_usuario")
    )
    @JsonIgnore
    private List<Usuario> usuarios;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "objEvento", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Formulario> formularios;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "objEvento", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Respuesta> respuestas;
}
