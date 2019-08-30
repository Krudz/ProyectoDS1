package maker.model;

import java.time.LocalDate;

public class Usuario extends Persona {

    private String cargo;
    private String contrasenna;
    private String telefono;

    public Usuario(String identificacion, String nombre, String apellido, LocalDate fechaNacimiento, String genero,
                   String domicilio, String cargo, String contraseña, String telefono) {
        super(identificacion, nombre, apellido, fechaNacimiento, genero, domicilio);
        this.cargo = cargo;
        this.contrasenna = contraseña;
        this.telefono = telefono;
    }


    public String getCargo() {
        return cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }

    public String getContrasenna() {
        return contrasenna;
    }

    public void setContrasenna(String contrasenna) {
        this.contrasenna = contrasenna;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }



}
