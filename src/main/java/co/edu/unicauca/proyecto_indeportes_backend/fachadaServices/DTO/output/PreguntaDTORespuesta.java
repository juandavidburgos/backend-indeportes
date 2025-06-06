package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class PreguntaDTORespuesta {
    private Integer id_pregunta;
    private String contenido_pregunta;
    private Integer obligatorio;
    private String tipo_pregunta;
    private List<RespuestaDTORespuesta> respuestas;

    
	public PreguntaDTORespuesta() {

	}

    public PreguntaDTORespuesta(Integer id_pregunta, String contenido_pregunta, Integer obligatorio, String tipo_pregunta) {
    this.id_pregunta = id_pregunta;
    this.contenido_pregunta = contenido_pregunta;
    this.obligatorio = obligatorio;
    this.tipo_pregunta = tipo_pregunta;
}

}
