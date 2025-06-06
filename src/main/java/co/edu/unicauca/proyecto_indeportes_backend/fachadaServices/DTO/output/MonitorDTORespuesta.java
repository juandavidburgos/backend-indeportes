package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MonitorDTORespuesta {
    private Integer id_usuario;
    private String nombre;
    private String email;
    private String contrasena;
    private String estado_monitor;
}
