package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.services;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.*;

import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Evento;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Formulario;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Pregunta;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Respuesta;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.models.Usuario;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.EventoRepository;
import co.edu.unicauca.proyecto_indeportes_backend.capaAccesoADatos.repositories.RespuestaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements IReporteService {

    private final EventoRepository eventoRepo;
    private final RespuestaRepository respuestaRepo;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<InputStreamResource> generarExcelReportePorEvento(Integer idEvento) throws IOException {
        Evento evento = eventoRepo.findById(idEvento).orElse(null);
        if (evento == null) {
            throw new IllegalArgumentException("Evento no encontrado");
        }

        List<Usuario> monitores = evento.getUsuarios();
        List<Formulario> formularios = evento.getFormularios();
        List<Respuesta> respuestas = respuestaRepo.findRespuestasByEventoId(evento.getId_evento());

        ByteArrayInputStream excelStream = generarExcelDesdeDatos(evento, monitores, formularios, respuestas);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=Reporte_Evento_" + evento.getId_evento() + ".xlsx");

        return ResponseEntity.ok()
                .headers(headers)
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(new InputStreamResource(excelStream));
    }

    private ByteArrayInputStream generarExcelDesdeDatos(Evento evento, List<Usuario> monitores, List<Formulario> formularios, List<Respuesta> respuestas) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Reporte Evento");
            int rowIdx = 0;

            // Estilo de título
            CellStyle titleStyle = workbook.createCellStyle();
            Font titleFont = workbook.createFont();
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 14);
            titleStyle.setFont(titleFont);
            titleStyle.setAlignment(HorizontalAlignment.CENTER);

            // Título principal
            Row titleRow = sheet.createRow(rowIdx++);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("M-GT-FOR-007 ESTADÍSTICAS DEPORTIVAS");
            titleCell.setCellStyle(titleStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));

            rowIdx++;

            // Información del evento
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Nombre del Evento: " + evento.getNombre());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Descripción: " + evento.getDescripcion());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Ubicación: " + evento.getUbicacion());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Fecha Inicio: " + evento.getFecha_hora_inicio());
            sheet.createRow(rowIdx++).createCell(0).setCellValue("Fecha Fin: " + evento.getFecha_hora_fin());

            rowIdx++;

            // Información de monitores
            Row monitoresTitle = sheet.createRow(rowIdx++);
            monitoresTitle.createCell(0).setCellValue("Monitores Asociados");

            for (Usuario monitor : monitores) {
                // Buscar el formulario del monitor en este evento
                Optional<Formulario> formularioDelMonitor = formularios.stream()
                        .filter(f -> f.getUsuario() != null && f.getUsuario().getId_usuario().equals(monitor.getId_usuario()))
                        .findFirst();

                String latitud = formularioDelMonitor.map(f -> String.valueOf(f.getLatitud())).orElse("N/A");
                String longitud = formularioDelMonitor.map(f -> String.valueOf(f.getLongitud())).orElse("N/A");
                String pathImagen = formularioDelMonitor.map(Formulario::getPath_imagen).orElse("N/A");


                Row row = sheet.createRow(rowIdx++);
                row.createCell(0).setCellValue(monitor.getNombre());
                row.createCell(1).setCellValue(monitor.getEmail());
                row.createCell(2).setCellValue(monitor.getRol().toString());
                row.createCell(3).setCellValue(latitud);
                row.createCell(4).setCellValue(longitud);
                row.createCell(5).setCellValue(pathImagen);
            }

            rowIdx++;

            // Obtener preguntas del primer formulario
            if (formularios.isEmpty()) {
                throw new IllegalArgumentException("El evento no tiene formularios.");
            }

            Formulario primerFormulario = formularios.get(0);
            List<Pregunta> preguntas = primerFormulario.getPreguntas();

            // Encabezado de preguntas
            Row encabezado = sheet.createRow(rowIdx++);
            for (int col = 0; col < preguntas.size(); col++) {
                encabezado.createCell(col).setCellValue(preguntas.get(col).getContenido());
            }

            // Agrupar respuestas por formulario (una fila por formulario)
            Map<Integer, List<Respuesta>> respuestasPorFormulario = new LinkedHashMap<>();
            for (Respuesta r : respuestas) {
                Integer idFormulario = r.getObjFormulario().getId_formulario();
                respuestasPorFormulario
                        .computeIfAbsent(idFormulario, k -> new ArrayList<>())
                        .add(r);
            }

            // Para cada formulario con respuestas, crear fila en el Excel
            for (Map.Entry<Integer, List<Respuesta>> entry : respuestasPorFormulario.entrySet()) {
                List<Respuesta> respuestasFormulario = entry.getValue();
                Row filaRespuesta = sheet.createRow(rowIdx++);

                for (int j = 0; j < preguntas.size(); j++) {
                    Pregunta p = preguntas.get(j);

                    Optional<Respuesta> respuesta = respuestasFormulario.stream()
                            .filter(r -> r.getObjPregunta().getId_pregunta().equals(p.getId_pregunta()))
                            .findFirst();

                    filaRespuesta.createCell(j).setCellValue(respuesta.map(Respuesta::getContenido).orElse(""));
                }
            }

            // Ajustar ancho de columnas
            for (int i = 0; i < preguntas.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        }
    }
}
