package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Respuesta;

public interface RespuestaRepository extends JpaRepository<Respuesta, Integer> {
    @Query(value = "SELECT * FROM Respuestas r WHERE r.id_evento = :idEvento", nativeQuery = true)
    List<Respuesta> findRespuestasByEventoId(@Param("idEvento") Integer idEvento);
}
