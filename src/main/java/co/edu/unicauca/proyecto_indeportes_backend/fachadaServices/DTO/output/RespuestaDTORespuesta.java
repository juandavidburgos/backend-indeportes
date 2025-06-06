package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RespuestaDTORespuesta {
    private Integer id_respuesta;
    private String contenido;
}
