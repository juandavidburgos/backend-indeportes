package co.edu.unicauca.proyecto_indeportes_backend.capaControladores;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EventoDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.EvidenciaDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.FormularioDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.FormularioRespondidoDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.RegistrarUsuarioDTOPeticion;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.EventoAsignacionDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.FormularioDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.output.MonitorDTORespuesta;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services.IEventoService;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services.IFormularioService;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services.IReporteService;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services.IUsuarioService;
import co.edu.unicauca.proyecto_indeportes_backend.seguridad.RequiereRol;

@RestController
@RequestMapping("/api")
@Validated
@CrossOrigin(origins = { "http://localhost:4200", "*" })
public class IndeportesRestController {

	@Autowired
	private IReporteService reporteService;

    @Autowired
    private IEventoService eventoService;

	@Autowired
	private IFormularioService formularioService;

	@Autowired
	private IUsuarioService usuarioService;

	@GetMapping("/asignaciones")
    public ResponseEntity<List<EventoAsignacionDTORespuesta>> listarAsignacionesPorEvento() {
        return ResponseEntity.ok(usuarioService.listarAsignacionesPorEvento());
    }

	@GetMapping("/monitores")
    public ResponseEntity<List<MonitorDTORespuesta>> listarMonitores() {
        return ResponseEntity.ok(usuarioService.listarMonitores());
    }

	@PostMapping("/registrar")
    public ResponseEntity<Usuario> registrarUsuario(@RequestBody RegistrarUsuarioDTOPeticion dto) {
        Usuario usuarioCreado = usuarioService.registrarUsuario(dto);
        return new ResponseEntity<>(usuarioCreado, HttpStatus.CREATED);
    }

	@GetMapping("/monitor/{idUsuario}/activos")
    public ResponseEntity<?> listarEventosActivosPorMonitor(@PathVariable Integer idUsuario) {
        List<Evento> eventos = eventoService.obtenerEventosActivosDelMonitor(idUsuario);
		 // ✅ Agrega aquí el print
		System.out.println("📤 Eventos encontrados para el usuario " + idUsuario + ":");
		for (Evento e : eventos) {
			System.out.println("🗂️ " + e.getId_evento() + " - " + e.getNombre() + " - " + e.getEstado());
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			String json = mapper.writeValueAsString(eventos);
			System.out.println("📦 JSON devuelto al cliente: " + json);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
        return ResponseEntity.ok(eventos);
    }
	
	@PostMapping("/formularios/responder")
	public ResponseEntity<?> registrarFormularioRespondido(@RequestBody FormularioRespondidoDTO dto) {
		try {
			formularioService.registrarRespuestasFormulario(dto);
			return ResponseEntity.ok("Formulario registrado exitosamente.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar formulario: " + e.getMessage());
		}
	}

	@PatchMapping("/formularios/{idFormulario}/evidencia")
	public ResponseEntity<String> registrarEvidencia(
		@PathVariable Integer idFormulario,
		@RequestBody EvidenciaDTO evidenciaDTO) {
		try {
			formularioService.registrarEvidencia(idFormulario, evidenciaDTO);
			return ResponseEntity.ok("Evidencia registrada correctamente.");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body("Error al registrar evidencia: " + e.getMessage());
		}
	}

	@RequiereRol("Administrador")
	@PostMapping("/{idUsuario}/asignar-evento/{idEvento}")
    public ResponseEntity<String> asignarEntrenador(@PathVariable Integer idUsuario, @PathVariable Integer idEvento) {
        usuarioService.asignarEntrenadorAEvento(idUsuario, idEvento);
        return ResponseEntity.ok("Entrenador asignado correctamente.");
    }

	@RequiereRol("Administrador")
    @GetMapping("/{idUsuario}/ubicacion")
    public ResponseEntity<String> obtenerUbicacion(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(usuarioService.verificarUbicacionEntrenador(idUsuario));
    }

	@RequiereRol("Administrador")
    @PatchMapping("/{idUsuario}/deshabilitar")
    public ResponseEntity<String> deshabilitarEntrenador(@PathVariable Integer idUsuario) {
        usuarioService.deshabilitarEntrenador(idUsuario);
        return ResponseEntity.ok("Entrenador deshabilitado correctamente.");
    }

	@RequiereRol("Administrador")
    @PatchMapping("/{idUsuario}/modificar-asignacion/{idEvento}/a/{nuevoIdEvento}")
	public ResponseEntity<String> modificarAsignacion(
		@PathVariable Integer idUsuario,
		@PathVariable Integer idEvento,
		@PathVariable Integer nuevoIdEvento
	) {
		usuarioService.modificarAsignacionEntrenador(idUsuario, idEvento, nuevoIdEvento);
		return ResponseEntity.ok("Asignación de entrenador modificada.");
	}

    
    @GetMapping("/eventos")
	public List<EventoDTOPeticion> index() {
		return eventoService.listarEventos();
	}

	@RequiereRol("Administrador")
	@PatchMapping("/eventos/{id}")
	public ResponseEntity<EventoDTOPeticion> actualizarParcialEvento(@PathVariable Integer id,
																	@RequestBody EventoDTOPeticion eventoParcial) {
		EventoDTOPeticion eventoActualizado = eventoService.modificarEvento(id, eventoParcial);
		return new ResponseEntity<>(eventoActualizado, HttpStatus.OK);
	}

	@RequiereRol("Administrador")
    @PostMapping("/eventos")
	public ResponseEntity<EventoDTOPeticion> create(@RequestBody EventoDTOPeticion evento) {
		EventoDTOPeticion objCliente = eventoService.crearEvento(evento);
		ResponseEntity<EventoDTOPeticion> objRespuesta = new ResponseEntity<EventoDTOPeticion>(objCliente, HttpStatus.CREATED);
		return objRespuesta;
	}


	@RequiereRol("Administrador")
	@DeleteMapping("/eventos/{id}")
	public Boolean delete(@PathVariable Integer id) {
		boolean bandera = false;
		bandera = eventoService.desactivarEvento(id);
		return bandera;
	}

	//@RequiereRol("Administrador")
	@GetMapping("/formularios/preguntas")
	public ResponseEntity<List<FormularioDTORespuesta>> obtenerFormulariosConPreguntas() {
		List<FormularioDTORespuesta> lista = formularioService.listarFormulariosPreguntas();
		return ResponseEntity.ok(lista);
	}

	@RequiereRol("Administrador")
	@PostMapping("/formularios")
	public ResponseEntity<FormularioDTOPeticion> create(@RequestBody FormularioDTOPeticion formulario) {
		FormularioDTOPeticion objCliente = formularioService.crearFormulario(formulario);
		ResponseEntity<FormularioDTOPeticion> objRespuesta = new ResponseEntity<FormularioDTOPeticion>(objCliente, HttpStatus.CREATED);
		return objRespuesta;
	}

	//@RequiereRol("Administrador")
	@GetMapping("/excel/{idEvento}")
    public ResponseEntity<InputStreamResource> exportarReporteExcel(@PathVariable Integer idEvento) throws IOException {
        return reporteService.generarExcelReportePorEvento(idEvento);
    }

}
