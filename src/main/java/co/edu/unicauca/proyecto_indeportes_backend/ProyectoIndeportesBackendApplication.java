package co.edu.unicauca.proyecto_indeportes_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ProyectoIndeportesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoIndeportesBackendApplication.class, args);
	}

}
