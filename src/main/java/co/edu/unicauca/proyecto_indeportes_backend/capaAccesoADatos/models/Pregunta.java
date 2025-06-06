package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models;

import java.util.List;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.TipoPregunta;
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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "Preguntas")
public class Pregunta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_pregunta;

    @Column(nullable = false)
    private String contenido;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoPregunta tipo;

    @Column(nullable = false)
    private Integer es_obligatoria = 1;

    @ManyToOne
    @JoinColumn(name = "formulario_id", nullable = false)
    private Formulario objFormulario;

    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "objPregunta", fetch = FetchType.LAZY)
    private List<Respuesta> respuestas;
}
