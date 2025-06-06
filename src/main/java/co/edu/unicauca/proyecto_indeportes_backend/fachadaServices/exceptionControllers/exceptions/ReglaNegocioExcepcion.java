package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions;

public class ReglaNegocioExcepcion extends GestionRuntimeException {

    private static final String FORMATO_EXCEPCION = "%s - Violación a regla de negocio: %s";
  
    private final String reglaNegocio;
  
    public ReglaNegocioExcepcion(final String reglaNegocio) {
      super(CodigoError.VIOLACION_REGLA_DE_NEGOCIO);
      this.reglaNegocio = reglaNegocio;
    }
  
    @Override
    public String formatException() {
      return String.format(FORMATO_EXCEPCION, codigoError.getCodigo(), reglaNegocio);
    }
  }
