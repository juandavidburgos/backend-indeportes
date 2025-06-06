package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.List;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EventoDTOPeticion;

public interface IEventoService {

    //crear el evento
    public EventoDTOPeticion crearEvento(EventoDTOPeticion evento);

    //listar eventos
    public List<EventoDTOPeticion> listarEventos();
    
    //desactivar evento
    public boolean desactivarEvento(Integer idEvento);

    // modificar evento existente
    public EventoDTOPeticion modificarEvento(Integer idEvento, EventoDTOPeticion eventoModificado);

    List<Evento> obtenerEventosActivosDelMonitor(Integer idUsuario);
}
