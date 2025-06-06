package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.exceptionControllers.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public abstract class GestionRuntimeException extends RuntimeException {

    protected CodigoError codigoError;

    public abstract String formatException();
    
}
