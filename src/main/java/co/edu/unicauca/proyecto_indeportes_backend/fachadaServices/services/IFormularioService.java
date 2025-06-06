package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.List;

import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EvidenciaDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.FormularioDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.FormularioRespondidoDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.FormularioDTORespuesta;

public interface IFormularioService {
    // Crear el formulario
    public FormularioDTOPeticion crearFormulario(FormularioDTOPeticion formulario);

    // Listar formularios
    public List<FormularioDTOPeticion> listarFormularios();

    public List<FormularioDTORespuesta> listarFormulariosPreguntas();

    public void registrarRespuestasFormulario(FormularioRespondidoDTO dto);

    public void registrarEvidencia(Integer idFormulario, EvidenciaDTO evidencia);
}
