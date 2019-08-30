package maker.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class UsuarioBD {

    private Connection connection;

    public UsuarioBD() {
        connection = new ConnectionFactory().getConnection();

    }

    public void registrar(Usuario usuario){

        String sql = "INSERT INTO public.Usuario (docIdUs, nombreUs, apellidoUs, fechaNacUs, telefonoUs, generoUs, direccionUs, cargoUs, passwordUs)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        statementSQL(usuario,sql);
    }

    public void actualizar(Usuario usuario){

        String sql = "UPDATE public.Usuario SET nombreUs=?, apellidoUs=?, fechaNacUs=?, telefonoUs=?, generoUs=?, direccionUs=?, cargoUs=?, passwordUs=?" +
                " WHERE docIdUs=?";

        statementSQL(usuario,sql);
    }

    public void inactivar(String identificacion){

        String sql = "DELETE FROM ds1.public.Usuario WHERE docIdUs=?";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, identificacion);

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public Usuario consultar(String identificacion){

        ObservableList<Usuario> usuarios = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ds1.public.Usuario WHERE docIdUs like '%" + identificacion + "%' ";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String cedula = resultSet.getString("docIdUs");
                String nombre = resultSet.getString("nombreUs");
                String apellido = resultSet.getString("apellidoUs");
                LocalDate fecha =  resultSet.getDate("fechaNacUs").toLocalDate();
                String telefono = resultSet.getString("telefonoUs");
                String genero = resultSet.getString("generoUs");
                String domicilio = resultSet.getString("direccionUs");
                String tipoDeCuenta = resultSet.getString("cargoUs");
                String contrasenna = resultSet.getString("passwordUs");

                Usuario usuario = new Usuario(cedula,nombre,apellido,fecha,genero,domicilio,tipoDeCuenta,contrasenna,telefono);

                return usuario;

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("se retorna el usuario nulo");
        return null;
    }

    //se acciona de acuerdo al sql.
    public void statementSQL(Usuario usuario, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, usuario.getIdentificacion());
            statement.setString(2, usuario.getNombre());
            statement.setString(3, usuario.getApellido());
            statement.setObject(4, usuario.getFechaNacimiento());
            statement.setString(5, usuario.getTelefono());
            statement.setString(6, usuario.getGenero());
            statement.setString(7, usuario.getDomicilio());
            statement.setString(8, usuario.getCargo());
            statement.setString(9, usuario.getContrasenna());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
