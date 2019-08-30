package maker.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.util.converter.LocalDateStringConverter;

import java.time.LocalDate;

public class Cliente extends Persona {

    private StringProperty email;
    private StringProperty telefono;

    public Cliente(String identificacion, String nombre, String apellido, LocalDate fechaNacimiento, String genero, String domicilio, String telefono, String email) {
        super(identificacion, nombre, apellido, fechaNacimiento, genero, domicilio);
        this.telefono = new SimpleStringProperty(telefono);
        this.email = new SimpleStringProperty(email);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() { return email; }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public String getTelefono() {
        return telefono.get();
    }

    public StringProperty telefonoProperty() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono.set(telefono);
    }

}
