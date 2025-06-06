package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import jakarta.transaction.Transactional;

public interface EventoRepository extends JpaRepository<Evento, Integer> { 

    @Transactional
    @Modifying
    @Query("UPDATE Evento e SET e.estado = 'inactivo' WHERE e.id_evento = :idEvento")
    public int desactivarEvento(Integer idEvento);

    @Transactional
    @Query(value = "SELECT e.* FROM eventos e " +
                   "INNER JOIN evento_usuario eu ON e.id_evento = eu.id_evento " +
                   "WHERE eu.id_usuario = :idUsuario AND e.estado = 'activo'", nativeQuery = true)
    List<Evento> findEventosActivosPorUsuario(@Param("idUsuario") Integer idUsuario);
}
