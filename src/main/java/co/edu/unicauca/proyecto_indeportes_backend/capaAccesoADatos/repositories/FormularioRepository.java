package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Formulario;

public interface FormularioRepository extends JpaRepository<Formulario, Integer> {   
     
    @Transactional
    @Query(value = "SELECT * FROM formularios f WHERE f.usuario_id = :idUsuario", nativeQuery = true)
    public Optional<Formulario> findByUsuarioId(Integer idUsuario);

    @Transactional
    @Query("SELECT f FROM Formulario f WHERE f.usuario.id_usuario = :idUsuario ORDER BY f.fecha_creacion DESC")
    Optional<Formulario> encontrarUltimoFormularioPorUsuario(@Param("idUsuario") Integer idUsuario);

    @Transactional
    @Query("SELECT f FROM Formulario f WHERE f.usuario.id_usuario = :idUsuario AND f.objEvento.id_evento = :idEvento")
    Optional<Formulario> findByUsuarioIdUsuarioAndObjEventoIdEvento(@Param("idUsuario") Integer idUsuario, @Param("idEvento") Integer idEvento);
}
