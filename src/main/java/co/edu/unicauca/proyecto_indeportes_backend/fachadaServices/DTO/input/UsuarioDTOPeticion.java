package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.Rol;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor

public class UsuarioDTOPeticion {
    private Integer id_usuario;
    private String nombre;
    private String email;
    private Rol rol;

    public UsuarioDTOPeticion() {

	}
}
