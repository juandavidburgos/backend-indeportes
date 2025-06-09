package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Formulario;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Pregunta;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Respuesta;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.EventoRepository;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.FormularioRepository;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.PreguntaRepository;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.RespuestaRepository;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EvidenciaDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.FormularioDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.FormularioRespondidoDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.RespuestaDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.FormularioDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.PreguntaDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.RespuestaDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.EntidadNoExisteException;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.EntidadYaExisteException;

@Service
public class FormularioServiceImpl implements IFormularioService {

    @Autowired
    private FormularioRepository servicioAccesoBaseDatos;

    @Autowired
    private EventoRepository servicioAccesoBaseDatos2;

    @Autowired
    private PreguntaRepository servicioAccesoBaseDatos3;

    @Autowired
    private RespuestaRepository servicioAccesoBaseDatos4;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
	@Qualifier("messageResourceSB")
	MessageSource messageSource;

    @Override
    @Transactional
    public FormularioDTOPeticion crearFormulario(FormularioDTOPeticion formulario) {
        System.out.println("Almacenando formulario: "+ formulario.getTitulo());
        if (formulario.getId_formulario() != null){
            final Boolean bandera = this.servicioAccesoBaseDatos.existsById(formulario.getId_formulario());
            if(bandera){
                EntidadYaExisteException objException = new EntidadYaExisteException(
						"Cliente con id " + formulario.getId_formulario() + " existe en la BD");
				throw objException;
            }
        }

        /*Revisar que validaciones se pueden hacer al guardar el evento (si el nombre puede ser repetido, la fecha, ubicacion, etc) */

        Formulario objFormulario = this.modelMapper.map(formulario, Formulario.class);
        Formulario objFormularioGuardado = this.servicioAccesoBaseDatos.save(objFormulario);
        FormularioDTOPeticion objFormularioGuardadoDTO = this.modelMapper.map(objFormularioGuardado, FormularioDTOPeticion.class);
        return objFormularioGuardadoDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<FormularioDTOPeticion> listarFormularios() {
        Iterable<Formulario> formularios = this.servicioAccesoBaseDatos.findAll();
        System.out.println("Antes de la consulta");
        List<FormularioDTOPeticion> formulariosDTO = this.modelMapper.map(formularios, new TypeToken<List<FormularioDTOPeticion>>(){}.getType());
        return formulariosDTO;
    }

    /*@Override
    @Transactional(readOnly = true)
    public List<FormularioDTORespuesta> listarFormulariosPreguntas() {
        Iterable<Formulario> listaFormularios = this.servicioAccesoBaseDatos.findAll();
        List<FormularioDTORespuesta> resultado = new ArrayList<>();
        
        for (Formulario formulario : listaFormularios) {

            FormularioDTORespuesta dto = new FormularioDTORespuesta();
            dto.setId_formulario(formulario.getId_formulario());
            dto.setTitulo(formulario.getTitulo());
            dto.setDescripcion(formulario.getDescripcion());
            dto.setFecha_creacion(formulario.getFecha_creacion());

            // ✅ Agregar id_evento e id_usuario
            dto.setId_evento(
                formulario.getObjEvento() != null ? formulario.getObjEvento().getId_evento() : null
            );

            dto.setId_usuario(
                formulario.getUsuario() != null ? formulario.getUsuario().getId_usuario() : null
            );



            List<PreguntaDTORespuesta> preguntasDTO = new ArrayList<>();
            for (Pregunta pregunta : formulario.getPreguntas()) {
                PreguntaDTORespuesta preguntaDTO = new PreguntaDTORespuesta();
                preguntaDTO.setId_pregunta(pregunta.getId_pregunta());
                preguntaDTO.setContenido_pregunta(pregunta.getContenido());
                preguntaDTO.setObligatorio(pregunta.getEs_obligatoria());
                preguntaDTO.setTipo_pregunta(pregunta.getTipo().toString());

                // Cargar respuestas asociadas a la pregunta
                List<RespuestaDTORespuesta> respuestasDTO = new ArrayList<>();
                for (Respuesta respuesta : pregunta.getRespuestas()) {
                    RespuestaDTORespuesta respuestaDTO = new RespuestaDTORespuesta();
                    respuestaDTO.setId_respuesta(respuesta.getId_respuesta());
                    respuestaDTO.setContenido(respuesta.getContenido());
                    respuestasDTO.add(respuestaDTO);
                }

                preguntaDTO.setRespuestas(respuestasDTO);
                preguntasDTO.add(preguntaDTO);
            }

            dto.setPreguntas(preguntasDTO);
            resultado.add(dto);
        }

        return resultado;
    }*/

    @Override
    @Transactional(readOnly = true)
    public List<FormularioDTORespuesta> listarFormulariosPreguntas() {
        Iterable<Formulario> listaFormularios = this.servicioAccesoBaseDatos.findAll();
        List<FormularioDTORespuesta> resultado = new ArrayList<>();

        for (Formulario formulario : listaFormularios) {
            // 🔁 Forzar carga de relaciones LAZY
            Integer idEvento = null;
            if (formulario.getObjEvento() != null) {
                idEvento = formulario.getObjEvento().getId_evento();
            }

            Integer idUsuario = null;
            if (formulario.getUsuario() != null) {
                idUsuario = formulario.getUsuario().getId_usuario();
            }

            FormularioDTORespuesta dto = new FormularioDTORespuesta();
            dto.setId_formulario(formulario.getId_formulario());
            dto.setTitulo(formulario.getTitulo());
            dto.setDescripcion(formulario.getDescripcion());
            dto.setFecha_creacion(formulario.getFecha_creacion());

            // ✅ Asignar ahora que están forzados los valores
            dto.setId_evento(idEvento);
            dto.setId_usuario(idUsuario);

            System.out.println("📥 Procesando formulario ID: " + formulario.getId_formulario());

            System.out.println("  ↪ objEvento: " + formulario.getObjEvento());
            System.out.println("  ↪ objEvento ID: " + (formulario.getObjEvento() != null ? formulario.getObjEvento().getId_evento() : "NULL"));
            System.out.println("  ↪ usuario: " + formulario.getUsuario());
            System.out.println("  ↪ usuario ID: " + (formulario.getUsuario() != null ? formulario.getUsuario().getId_usuario() : "NULL"));


           /*/ // 🔁 Procesar preguntas
            List<PreguntaDTORespuesta> preguntasDTO = new ArrayList<>();
            for (Pregunta pregunta : formulario.getPreguntas()) {
                PreguntaDTORespuesta preguntaDTO = new PreguntaDTORespuesta();
                preguntaDTO.setId_pregunta(pregunta.getId_pregunta());
                preguntaDTO.setContenido_pregunta(pregunta.getContenido());
                preguntaDTO.setObligatorio(pregunta.getEs_obligatoria());
                preguntaDTO.setTipo_pregunta(pregunta.getTipo().toString());

                List<RespuestaDTORespuesta> respuestasDTO = new ArrayList<>();
                for (Respuesta respuesta : pregunta.getRespuestas()) {
                    RespuestaDTORespuesta respuestaDTO = new RespuestaDTORespuesta();
                    respuestaDTO.setId_respuesta(respuesta.getId_respuesta());
                    respuestaDTO.setContenido(respuesta.getContenido());
                    respuestasDTO.add(respuestaDTO);
                }

                preguntaDTO.setRespuestas(respuestasDTO);
                preguntasDTO.add(preguntaDTO);
            }

            dto.setPreguntas(preguntasDTO);
            resultado.add(dto);*/

            List<PreguntaDTORespuesta> preguntasDTO = new ArrayList<>();
            System.out.println("📦 Procesando preguntas para el formulario ID: " + formulario.getId_formulario());

            for (Pregunta pregunta : formulario.getPreguntas()) {
                System.out.println("🔍 Pregunta ID: " + pregunta.getId_pregunta() + " → Contenido: " + pregunta.getContenido());
                
                PreguntaDTORespuesta preguntaDTO = new PreguntaDTORespuesta();
                preguntaDTO.setId_pregunta(pregunta.getId_pregunta());
                preguntaDTO.setContenido_pregunta(pregunta.getContenido());
                preguntaDTO.setObligatorio(pregunta.getEs_obligatoria());
                preguntaDTO.setTipo_pregunta(pregunta.getTipo().toString());

                List<RespuestaDTORespuesta> respuestasDTO = new ArrayList<>();
                for (Respuesta respuesta : pregunta.getRespuestas()) {
                    System.out.println("   ↳ Respuesta ID: " + respuesta.getId_respuesta() + " → Contenido: " + respuesta.getContenido());
                    
                    RespuestaDTORespuesta respuestaDTO = new RespuestaDTORespuesta();
                    respuestaDTO.setId_respuesta(respuesta.getId_respuesta());
                    respuestaDTO.setContenido(respuesta.getContenido());
                    respuestasDTO.add(respuestaDTO);
                }

                preguntaDTO.setRespuestas(respuestasDTO);
                preguntasDTO.add(preguntaDTO);
            }

            System.out.println("✅ Total preguntas procesadas para el formulario: " + preguntasDTO.size());

            dto.setPreguntas(preguntasDTO);
            resultado.add(dto);

        }

        return resultado;
    }


    @Override
    @Transactional
    public void registrarEvidencia(Integer idFormulario, EvidenciaDTO evidencia) {
        Formulario formulario = servicioAccesoBaseDatos.findById(idFormulario)
            .orElseThrow(() -> new RuntimeException("Formulario no encontrado"));

        formulario.setLatitud(evidencia.getLatitud());
        formulario.setLongitud(evidencia.getLongitud());
        formulario.setPath_imagen(evidencia.getPath_imagen());

        servicioAccesoBaseDatos.save(formulario);
    }



    @Override
    @Transactional
    public void registrarRespuestasFormulario(FormularioRespondidoDTO dto) {
        Formulario formulario = servicioAccesoBaseDatos.findById(dto.getIdFormulario())
        .orElseThrow(() -> new EntidadNoExisteException("Formulario no encontrado"));

        Evento evento = servicioAccesoBaseDatos2.findById(dto.getIdEvento())
        .orElseThrow(() -> new EntidadNoExisteException("Evento no encontrado"));

    for (RespuestaDTO respuestaDTO : dto.getRespuestas()) {
        Pregunta pregunta = servicioAccesoBaseDatos3.findById(respuestaDTO.getPregunta_id())
            .orElseThrow(() -> new EntidadNoExisteException("Pregunta no encontrada"));

        Respuesta respuesta = new Respuesta();
        respuesta.setContenido(respuestaDTO.getContenido());
        respuesta.setObjFormulario(formulario);
        respuesta.setObjPregunta(pregunta);
        respuesta.setObjEvento(evento);

        servicioAccesoBaseDatos4.save(respuesta);
    }
    }

}
