package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class EventoDTOPeticion {
    private Long id_evento;
    private String nombre;
    private String descripcion;
    private String ubicacion;
    private String fecha_hora_inicio;
    private String fecha_hora_fin;
    private String estado;

    public EventoDTOPeticion() {}

}
