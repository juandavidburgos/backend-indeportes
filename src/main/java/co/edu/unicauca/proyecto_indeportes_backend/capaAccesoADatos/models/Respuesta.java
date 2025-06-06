package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity
@Table(name = "Respuestas")
public class Respuesta {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id_respuesta;

    @Column(nullable = false)
    private String contenido;

    @ManyToOne
    @JoinColumn(name = "pregunta_id", nullable = false)
    private Pregunta objPregunta;

    @ManyToOne
    @JoinColumn(name = "formulario_id", nullable = false)
    private Formulario objFormulario;

    @ManyToOne
    @JoinColumn(name = "id_evento", nullable = false)
    private Evento objEvento;

}
