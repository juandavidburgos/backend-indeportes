package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.List;

import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.PreguntaDTOPeticion;

public interface IPreguntaService {
    //crear pregunta
    public PreguntaDTOPeticion crearPregunta(PreguntaDTOPeticion pregunta);

    //listar preguntas
    public List<PreguntaDTOPeticion> listarPreguntas();
}
