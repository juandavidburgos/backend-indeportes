package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.EstadoMonitor;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrarUsuarioDTOPeticion {
    Integer id_usuario;
    String nombre;
    String email;
    String contrasena;
    Rol rol;
    EstadoMonitor estado_monitor;
}
