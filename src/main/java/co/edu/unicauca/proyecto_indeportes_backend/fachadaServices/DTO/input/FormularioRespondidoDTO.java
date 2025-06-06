package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FormularioRespondidoDTO {
    private Integer idFormulario;
    private Integer idEvento;
    private List<RespuestaDTO> respuestas;
}
