package co.edu.unicauca.proyecto_indeportes_backend.fachadaServices.DTO.input;

public class LoginDTO {
    private String email;
    private String contrasena;

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getContrasena() {
        return contrasena;
    }
    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }
}
