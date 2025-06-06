package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "Formularios")
public class Formulario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_formulario;

    @Column(nullable = false)
    private String titulo;

    @Column
    private String descripcion;

    @Column(nullable = false)
    private Date fecha_creacion;

    @Column
    private Float latitud;

    @Column
    private Float longitud;

    @Column
    private String path_imagen;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = true)
    private Evento objEvento;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = true)
    private Usuario usuario;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "objFormulario", fetch = FetchType.LAZY)
    private List<Pregunta> preguntas;
}
