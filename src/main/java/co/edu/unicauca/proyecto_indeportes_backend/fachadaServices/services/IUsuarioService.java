package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.List;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.RegistrarUsuarioDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.EventoAsignacionDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.MonitorDTORespuesta;
import jakarta.servlet.http.HttpSession;

public interface IUsuarioService {
    boolean asignarEntrenadorAEvento(Integer idUsuario, Integer idEvento);
    String verificarUbicacionEntrenador(Integer idUsuario);
    boolean deshabilitarEntrenador(Integer idUsuario);
    boolean modificarAsignacionEntrenador(Integer idUsuario,Integer idEvento, Integer nuevoIdEvento);
    Usuario login(String email, String contrasena, HttpSession session);
    public Usuario registrarUsuario(RegistrarUsuarioDTOPeticion dto);
    List<MonitorDTORespuesta> listarMonitores();
    List<EventoAsignacionDTORespuesta> listarAsignacionesPorEvento();
}
