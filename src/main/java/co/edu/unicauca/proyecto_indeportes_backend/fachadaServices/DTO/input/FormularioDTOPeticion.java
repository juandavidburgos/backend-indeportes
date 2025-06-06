package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor

public class FormularioDTOPeticion {
    private Integer id_formulario;
    private String titulo;
    private String descripcion;
    private Date fecha_creacion;

    

    public FormularioDTOPeticion() {

	}
}
