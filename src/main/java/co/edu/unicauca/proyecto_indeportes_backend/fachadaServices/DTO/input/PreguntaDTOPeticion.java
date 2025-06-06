package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.TipoPregunta;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor


public class PreguntaDTOPeticion {

    private Integer id_pregunta;
    private String contenido_pregunta;
    private TipoPregunta tipo_pregunta;
    private Boolean obligatorio;
    private Integer orden;

     
	public PreguntaDTOPeticion() {

	}
}
