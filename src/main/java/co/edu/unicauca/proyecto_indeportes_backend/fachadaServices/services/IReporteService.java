package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import java.io.IOException;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;


public interface IReporteService {
    public ResponseEntity<InputStreamResource> generarExcelReportePorEvento(Integer eventoId) throws IOException;
}
