package maker.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class ProveedorBD {

    private Connection connection;

    public ProveedorBD() {
        connection = new ConnectionFactory().getConnection();

    }

    public ObservableList<Proveedor> mostrartable(){

        ObservableList<Proveedor> listaProveedores = FXCollections.observableArrayList();

        String sql = "SELECT codigoProv, nitProv, nombreProv, direccionProv , telefonoProv, emailProv FROM ds1.public.proveedor";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String codigo = resultSet.getString("codigoProv");
                String nit = resultSet.getString("nitProv");
                String nombre = resultSet.getString("nombreProv");
                String direccion = resultSet.getString("direccionProv");
                String telefono = resultSet.getString("telefonoProv");
                String email = resultSet.getString("emailProv");

                Proveedor proveedor = new Proveedor(codigo,nit,nombre,direccion,telefono,email);

                listaProveedores.add(proveedor);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listaProveedores;
    }

    public void registrar(Proveedor proveedor){

        String sql = "INSERT INTO public.Proveedor (codigoProv, nitProv, nombreProv, direccionProv, telefonoProv, emailProv)" +
                " VALUES (?, ?, ?, ?, ?, ?)";

        statementSQL(proveedor,sql);
    }

    public void actualizar(Proveedor proveedor){

        String sql = "UPDATE public.Proveedor SET nitProv=?, nombreProv=?, direccionProv=?, telefonoProv=?, emailProv=?" +
                " WHERE codigoProv=?";

        statementSQLUPDATE(proveedor,sql);
    }

    public void inactivar(String identificacion){

        String sql = "DELETE FROM ds1.public.Proveedor WHERE codigoProv=?";

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

    public Proveedor consultar(String identificacion){

        ObservableList<Proveedor> proveedors = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ds1.public.Proveedor WHERE codigoProv like '%" + identificacion + "%' ";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String codigo = resultSet.getString("codigoProv");
                String nit = resultSet.getString("nitProv");
                String nombre = resultSet.getString("nombreProv");
                String direccion = resultSet.getString("direccionProv");
                String telefono = resultSet.getString("telefonoProv");
                String email = resultSet.getString("emailProv");

                Proveedor proveedor = new Proveedor(codigo,nit,nombre,direccion,telefono,email);

                return proveedor;

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
    public void statementSQL(Proveedor proveedor, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, proveedor.getCodigo());
            statement.setString(2, proveedor.getNit());
            statement.setString(3, proveedor.getNombre());
            statement.setString(4, proveedor.getDireccion());
            statement.setString(5, proveedor.getTelefono());
            statement.setString(6, proveedor.getEmail());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public void statementSQLUPDATE(Proveedor proveedor, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, proveedor.getNit());
            statement.setString(2, proveedor.getNombre());
            statement.setString(3, proveedor.getDireccion());
            statement.setString(4, proveedor.getTelefono());
            statement.setString(5, proveedor.getEmail());
            statement.setString(6, proveedor.getCodigo());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
