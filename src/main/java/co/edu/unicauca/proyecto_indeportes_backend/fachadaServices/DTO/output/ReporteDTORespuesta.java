package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output;

import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EventoDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.UsuarioDTOPeticion;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ReporteDTORespuesta {
    private EventoDTOPeticion evento;
    private UsuarioDTOPeticion usuario;
    private FormularioDTORespuesta formato;

    public ReporteDTORespuesta() {
    }
}
