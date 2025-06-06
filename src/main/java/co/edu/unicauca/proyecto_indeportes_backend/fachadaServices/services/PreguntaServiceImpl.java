package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Pregunta;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.PreguntaRepository;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.PreguntaDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.EntidadYaExisteException;

import org.springframework.transaction.annotation.Transactional;

@Service
public class PreguntaServiceImpl implements IPreguntaService{

    @Autowired
    private PreguntaRepository servicioAccesoBaseDatos;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
	@Qualifier("messageResourceSB")
	MessageSource messageSource;

    @Override
    @Transactional
    public PreguntaDTOPeticion crearPregunta(PreguntaDTOPeticion pregunta) {
        System.out.println("Almacenando pregunta: "+ pregunta.getContenido_pregunta());
        if (pregunta.getId_pregunta() != null){
            final Boolean bandera = this.servicioAccesoBaseDatos.existsById(pregunta.getId_pregunta());
            if(bandera){
                EntidadYaExisteException objException = new EntidadYaExisteException(
						"Cliente con id " + pregunta.getId_pregunta() + " existe en la BD");
				throw objException;
            }
        }

        /*Revisar que validaciones se pueden hacer al guardar el evento (si el nombre puede ser repetido, la fecha, ubicacion, etc) */

        Pregunta objPregunta = this.modelMapper.map(pregunta, Pregunta.class);
        Pregunta objPreguntaGuardado = this.servicioAccesoBaseDatos.save(objPregunta);
        PreguntaDTOPeticion objPreguntaGuardadoDTO = this.modelMapper.map(objPreguntaGuardado, PreguntaDTOPeticion.class);
        return objPreguntaGuardadoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PreguntaDTOPeticion> listarPreguntas() {
        Iterable<Pregunta> preguntas = this.servicioAccesoBaseDatos.findAll();
        System.out.println("Antes de la consulta");
        List<PreguntaDTOPeticion> preguntasDTO = this.modelMapper.map(preguntas, new TypeToken<List<PreguntaDTOPeticion>>(){}.getType());
        return preguntasDTO;
    }
    
}
