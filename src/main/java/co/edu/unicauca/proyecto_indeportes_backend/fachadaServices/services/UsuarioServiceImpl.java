package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.EstadoMonitor;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.Rol;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Formulario;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Pregunta;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.EventoRepository;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.FormularioRepository;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.UsuarioRepository;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.RegistrarUsuarioDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.EventoAsignacionDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.MonitorDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.EntidadNoExisteException;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions.ReglaNegocioExcepcion;
import jakarta.servlet.http.HttpSession;

@Service
public class UsuarioServiceImpl implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private FormularioRepository formularioRepository;

    @Override
    public List<MonitorDTORespuesta> listarMonitores() {
        return usuarioRepository.findByRol(Rol.Monitor).stream()
                .map(u -> new MonitorDTORespuesta(
                        u.getId_usuario(),
                        u.getNombre(),
                        u.getEmail(),
                        u.getContrasena(),
                        u.getEstadoMonitor().toString()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventoAsignacionDTORespuesta> listarAsignacionesPorEvento() {
        return eventoRepository.findAll().stream()
                .filter(e -> e.getUsuarios() != null && !e.getUsuarios().isEmpty())
                .map(evento -> {
                    List<MonitorDTORespuesta> monitores = evento.getUsuarios().stream()
                            .filter(u -> u.getRol() == Rol.Monitor)
                            .map(u -> new MonitorDTORespuesta(
                                    u.getId_usuario(),
                                    u.getNombre(),
                                    u.getEmail(),
                                    u.getContrasena(),
                                    u.getEstadoMonitor().toString()))
                            .collect(Collectors.toList());
                    
                    return new EventoAsignacionDTORespuesta(
                            evento.getId_evento(),
                            evento.getNombre(),
                            evento.getFecha_hora_inicio(),
                            evento.getFecha_hora_fin(),
                            evento.getUbicacion(),
                            monitores);
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Usuario registrarUsuario(RegistrarUsuarioDTOPeticion dto) {
        Usuario usuario = new Usuario();

        usuario.setNombre(dto.getNombre());
        usuario.setEmail(dto.getEmail());
        usuario.setContrasena(dto.getContrasena());
        usuario.setRol(dto.getRol());
        usuario.setEstadoMonitor(dto.getEstado_monitor());

        return usuarioRepository.save(usuario);
    }

    @Override
    @Transactional
    public boolean asignarEntrenadorAEvento(Integer idUsuario, Integer idEvento) {
        try {
            Usuario usuario = validarEntrenadorActivo(idUsuario);
            Evento evento = eventoRepository.findById(idEvento)
                    .orElseThrow(() -> new EntidadNoExisteException("Evento no encontrado"));

            if (evento.getUsuarios().stream().anyMatch(u -> u.getId_usuario().equals(idUsuario))) {
                throw new IllegalStateException("El entrenador ya está asignado al evento");
            }


            usuario.getEventos().add(evento);
            evento.getUsuarios().add(usuario);

            Formulario formularioBase = formularioRepository.findById(1)
                    .orElseThrow(() -> new EntidadNoExisteException("Formulario base (ID = 1) no encontrado"));

            Formulario formularioClonado = new Formulario();
            formularioClonado.setTitulo(formularioBase.getTitulo());
            formularioClonado.setDescripcion(formularioBase.getDescripcion());
            formularioClonado.setFecha_creacion(new Date());
            formularioClonado.setLatitud(null);
            formularioClonado.setLongitud(null);
            formularioClonado.setPath_imagen(null);
            formularioClonado.setObjEvento(evento);
            formularioClonado.setUsuario(usuario);

            List<Pregunta> preguntasClonadas = formularioBase.getPreguntas().stream().map(p -> {
                Pregunta nueva = new Pregunta();
                nueva.setContenido(p.getContenido());
                nueva.setTipo(p.getTipo());
                nueva.setEs_obligatoria(p.getEs_obligatoria());
                nueva.setObjFormulario(formularioClonado);
                return nueva;
            }).toList();

            formularioClonado.setPreguntas(preguntasClonadas);

            System.out.println("➡️ Guardando formulario clonado");
            System.out.println("   ID evento: " + (evento != null ? evento.getId_evento() : "NULL"));
            System.out.println("   ID usuario: " + (usuario != null ? usuario.getId_usuario() : "NULL"));


            formularioRepository.save(formularioClonado);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional(readOnly = true)
    public String verificarUbicacionEntrenador(Integer idUsuario) {
        Usuario usuario = validarEntrenadorActivo(idUsuario);

        Formulario formulario = formularioRepository.encontrarUltimoFormularioPorUsuario(idUsuario)
                .orElseThrow(() -> new EntidadNoExisteException(
                        "No se encontró formulario para el usuario con ID " + idUsuario));

        return "Latitud: " + formulario.getLatitud() + ", Longitud: " + formulario.getLongitud();
    }

   @Override
    @Transactional
    public boolean deshabilitarEntrenador(Integer idUsuario) {
        try {
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new EntidadNoExisteException("Usuario con id " + idUsuario + " no existe"));

            if (usuario.getRol() == null || !usuario.getRol().name().equalsIgnoreCase("MONITOR")) {
                throw new ReglaNegocioExcepcion("Solo los monitores pueden ser deshabilitados.");
            }

            int filasAfectadas = usuarioRepository.deshabilitarEntrenador(idUsuario);
            return filasAfectadas > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    @Transactional
    public boolean modificarAsignacionEntrenador(Integer idUsuario, Integer idEvento, Integer nuevoIdEvento) {
        try {
            Usuario usuario = validarEntrenadorActivo(idUsuario);

            Evento eventoAnterior = eventoRepository.findById(idEvento)
                    .orElseThrow(() -> new EntidadNoExisteException("Evento anterior no encontrado"));
            Evento nuevoEvento = eventoRepository.findById(nuevoIdEvento)
                    .orElseThrow(() -> new EntidadNoExisteException("Nuevo evento no encontrado"));

            if (usuario.getEventos().stream().anyMatch(e -> e.getId_evento().equals(nuevoIdEvento))) {
                return true;
            }

            eliminarFormularioPorUsuarioYEvento(idUsuario, idEvento);

            usuario.getEventos().remove(eventoAnterior);
            eventoAnterior.getUsuarios().remove(usuario);

            usuario.getEventos().add(nuevoEvento);
            nuevoEvento.getUsuarios().add(usuario);

            Formulario formularioBase = formularioRepository.findById(1)
                    .orElseThrow(() -> new EntidadNoExisteException("Formulario base (ID = 1) no encontrado"));

            Formulario formularioClonado = new Formulario();
            formularioClonado.setTitulo(formularioBase.getTitulo());
            formularioClonado.setDescripcion(formularioBase.getDescripcion());
            formularioClonado.setFecha_creacion(new Date());
            formularioClonado.setLatitud(null);
            formularioClonado.setLongitud(null);
            formularioClonado.setPath_imagen(null);
            formularioClonado.setObjEvento(nuevoEvento);
            formularioClonado.setUsuario(usuario);

            List<Pregunta> preguntasClonadas = formularioBase.getPreguntas().stream().map(p -> {
                Pregunta nueva = new Pregunta();
                nueva.setContenido(p.getContenido());
                nueva.setTipo(p.getTipo());
                nueva.setEs_obligatoria(p.getEs_obligatoria());
                nueva.setObjFormulario(formularioClonado);
                return nueva;
            }).toList();

            formularioClonado.setPreguntas(preguntasClonadas);

            formularioRepository.save(formularioClonado);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void eliminarFormularioPorUsuarioYEvento(Integer idUsuario, Integer idEvento) {
        Optional<Formulario> formularioOpt = formularioRepository.findByUsuarioIdUsuarioAndObjEventoIdEvento(idUsuario, idEvento);

        if (formularioOpt.isPresent()) {
            Formulario formulario = formularioOpt.get();

            formulario.getPreguntas().clear();

            formularioRepository.save(formulario);

            formularioRepository.delete(formulario);
        }
    }



    private Usuario validarEntrenadorActivo(Integer idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new EntidadNoExisteException("Usuario no encontrado"));

        if (usuario.getEstadoMonitor() != EstadoMonitor.activo) {
            throw new ReglaNegocioExcepcion("El usuario con ID " + idUsuario + " no está activo como entrenador.");
        }

        return usuario;
    }

    @Transactional
    public Usuario login(String email, String contrasena, HttpSession session) {
    Usuario usuario = usuarioRepository.findByEmail(email)
                      .orElseThrow(() -> new RuntimeException("Email o contraseña inválidos"));
    if (!usuario.getContrasena().equals(contrasena)) {
        throw new ReglaNegocioExcepcion("Email o contraseña inválidos");
    }
    session.setAttribute("usuarioLogueado", usuario);
    return usuario;
}
}
