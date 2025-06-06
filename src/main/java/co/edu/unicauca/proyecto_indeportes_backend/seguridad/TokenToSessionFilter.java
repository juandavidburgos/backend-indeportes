package co.edu.unicauca.proyecto_indeportes_backend.seguridad;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.EstadoMonitor;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.Rol;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.util.Map;

@Component
public class TokenToSessionFilter implements Filter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpReq = (HttpServletRequest) request;
        HttpSession session = httpReq.getSession(false);

        if ((session == null || session.getAttribute("usuarioLogueado") == null)) {
            String authHeader = httpReq.getHeader("Authorization");

            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                try {
                    String token = authHeader.substring(7); // Quita el "Bearer "
                    Map<String, Object> userMap = objectMapper.readValue(token, Map.class);

                    Usuario usuario = new Usuario();
                    usuario.setId_usuario((Integer) userMap.get("id_usuario"));
                    usuario.setNombre((String) userMap.get("nombre"));
                    usuario.setEmail((String) userMap.get("email"));
                    usuario.setRol(Rol.valueOf((String) userMap.get("rol")));
                    usuario.setEstadoMonitor(EstadoMonitor.valueOf((String) userMap.get("estado_monitor")));

                    HttpSession newSession = httpReq.getSession(true);
                    newSession.setAttribute("usuarioLogueado", usuario);

                } catch (Exception e) {
                    System.out.println("Error al parsear token: " + e.getMessage());
                }
            }
        }

        chain.doFilter(request, response);
    }
}
