package co.edu.unicauca.proyecto_indeportes_backend.capaControladores;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input.LoginDTO;
import co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services.IUsuarioService;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    private final IUsuarioService usuarioService;

    public AuthController(IUsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDTO loginDTO, HttpSession session) throws JsonProcessingException {
        try {
            Usuario usuario = usuarioService.login(
                loginDTO.getEmail(),
                loginDTO.getContrasena(),
                session
            );

            // 📦 Aquí construyes manualmente el contenido del token (como un payload de JWT)
            Map<String, Object> userPayload = new HashMap<>();
            userPayload.put("id_usuario", usuario.getId_usuario());
            userPayload.put("nombre", usuario.getNombre());
            userPayload.put("email", usuario.getEmail());
            userPayload.put("rol", usuario.getRol());
            userPayload.put("estado_monitor", usuario.getEstadoMonitor());

            // Lo serializas a string para enviarlo como un "token"
            ObjectMapper mapper = new ObjectMapper();
            String token = mapper.writeValueAsString(userPayload); // ← Es un JSON plano

            Map<String, Object> response = new HashMap<>();
            response.put("token", token);
            System.out.println("EMAIL RECIBIDO: " + loginDTO.getEmail());
            System.out.println("CONTRASEÑA RECIBIDA: " + loginDTO.getContrasena());

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.out.println("EMAIL RECIBIDO en excepcion: " + loginDTO.getEmail());
            System.out.println("CONTRASEÑA RECIBIDA: " + loginDTO.getContrasena());

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok("Sesión cerrada");
    }
}