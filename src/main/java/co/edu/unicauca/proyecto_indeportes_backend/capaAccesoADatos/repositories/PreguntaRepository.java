package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Pregunta;

public interface PreguntaRepository extends JpaRepository<Pregunta, Integer> {
    
}
