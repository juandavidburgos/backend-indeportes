package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class FormularioDTORespuesta {
    private Integer id_formulario;
    private String titulo;
    private String descripcion;
    private Date fecha_creacion;
    private List<PreguntaDTORespuesta> preguntas;

    private Integer id_evento;
    private Integer id_usuario;

	public FormularioDTORespuesta() {

	}
}