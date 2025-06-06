package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.EventoRepository;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EventoDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.EntidadNoExisteException;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.EntidadYaExisteException;

@Service
public class EventoServiceImpl implements IEventoService{

    @Autowired
    private EventoRepository servicioAccesoBaseDatos;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
	@Qualifier("messageResourceSB")
	MessageSource messageSource;

    @Override
    @Transactional
    public EventoDTOPeticion modificarEvento(Integer idEvento, EventoDTOPeticion eventoModificado) {
        Evento eventoExistente = servicioAccesoBaseDatos.findById(idEvento)
            .orElseThrow(() -> new EntidadNoExisteException("El evento con ID " + idEvento + " no existe."));

        // Actualizar solo los campos no nulos del DTO
        if (eventoModificado.getNombre() != null) {
            eventoExistente.setNombre(eventoModificado.getNombre());
        }
        if (eventoModificado.getDescripcion() != null) {
            eventoExistente.setDescripcion(eventoModificado.getDescripcion());
        }
        if (eventoModificado.getFecha_hora_inicio() != null) {
            eventoExistente.setFecha_hora_inicio(eventoModificado.getFecha_hora_inicio());
        }
        if (eventoModificado.getFecha_hora_fin() != null) {
            eventoExistente.setFecha_hora_fin(eventoModificado.getFecha_hora_fin());
        }
        if (eventoModificado.getUbicacion() != null) {
            eventoExistente.setUbicacion(eventoModificado.getUbicacion());
        }
        Evento eventoActualizado = servicioAccesoBaseDatos.save(eventoExistente);
        return modelMapper.map(eventoActualizado, EventoDTOPeticion.class);
    }

    @Override
    @Transactional
    public EventoDTOPeticion crearEvento(EventoDTOPeticion evento) {
        Evento objEvento = this.modelMapper.map(evento, Evento.class);
        System.out.println("Almacenando evento: " + evento.getNombre());

        if (objEvento.getId_evento() != null) {
            boolean existe = this.servicioAccesoBaseDatos.existsById(objEvento.getId_evento());
            if (existe) {
                throw new EntidadYaExisteException(
                    "Evento con id " + objEvento.getId_evento() + " ya existe en la BD");
            }
        }

        // Guardar evento
        Evento objEventoGuardado = this.servicioAccesoBaseDatos.save(objEvento);
        return this.modelMapper.map(objEventoGuardado, EventoDTOPeticion.class);
    }
    @Override
    @Transactional(readOnly = true)
    public List<EventoDTOPeticion> listarEventos() {
        Iterable<Evento> eventos = this.servicioAccesoBaseDatos.findAll();
        System.out.println("Antes de la consulta");
        List<EventoDTOPeticion> eventosDTO = this.modelMapper.map(eventos, new TypeToken<List<EventoDTOPeticion>>(){}.getType());
        return eventosDTO;
    }

    @Override
    @Transactional
    public boolean desactivarEvento(Integer idEvento) {
        System.out.println("Desactivando evento: "+ idEvento);
        if (idEvento != null){
            final Boolean bandera = this.servicioAccesoBaseDatos.existsById(idEvento);
            if(!bandera){
                EntidadNoExisteException objException = new EntidadNoExisteException(
						"Evento con id " + idEvento + " existe en la BD");
				throw objException;
            }
        }
        int filasAfectadas = this.servicioAccesoBaseDatos.desactivarEvento(idEvento);
        return filasAfectadas > 0;
    }

    @Override
    public List<Evento> obtenerEventosActivosDelMonitor(Integer idUsuario) {
        return servicioAccesoBaseDatos.findEventosActivosPorUsuario(idUsuario);
    }
    
}
