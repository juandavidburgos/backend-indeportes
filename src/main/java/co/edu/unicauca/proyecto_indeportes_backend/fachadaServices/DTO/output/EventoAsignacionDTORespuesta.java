package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventoAsignacionDTORespuesta {
    private Integer id_evento;
    private String nombre;
    private String fecha_hora_inicio;
    private String fecha_hora_fin;
    private String ubicacion;
    private List<MonitorDTORespuesta> monitoresAsignados;
}
