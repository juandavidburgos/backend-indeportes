package co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.Enums.Rol;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    @Transactional
    @Modifying
    @Query("UPDATE Usuario u SET u.estadoMonitor = 'inactivo' WHERE u.id_usuario = :idUsuario")
    public int deshabilitarEntrenador(Integer idUsuario);

    @Transactional
    @Modifying
    @Query(value = "UPDATE evento_usuario SET id_evento = :nuevoIdEvento WHERE id_usuario = :idUsuario AND id_evento = :idEvento", nativeQuery = true)
    int actualizarAsignacionEvento(Integer idUsuario, Integer idEvento, Integer nuevoIdEvento);
    
    @Transactional
    Optional<Usuario> findByEmail(String email);

    @Transactional
    List<Usuario> findByRol(Rol rol);
}
