package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class RespuestaDTOPeticion {
    
    private Integer id_respuesta;
    private String respuesta;

    public RespuestaDTOPeticion() {

	}
}
