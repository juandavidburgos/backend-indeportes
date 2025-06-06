package co.edu.unicauca.proyecto_indeportes_backend.seguridad;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class SeguridadAspecto {

    private final HttpSession session;

    public SeguridadAspecto(HttpSession session) {
        this.session = session;
    }

    @Around("@annotation(co.edu.unicauca.proyecto_indeportes_backend.seguridad.RequiereRol)")
    public Object verificarRol(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        RequiereRol requiereRol = method.getAnnotation(RequiereRol.class);
        String rolNecesario = requiereRol.value();

        Usuario usuario = (Usuario) session.getAttribute("usuarioLogueado");
        if (usuario == null) {
            throw new RuntimeException("No está autenticado");
        }

        if (!usuario.getRol().toString().equalsIgnoreCase(rolNecesario)) {
            throw new RuntimeException("No tiene permiso para esta operación");
        }

        return joinPoint.proceed();
    }
}