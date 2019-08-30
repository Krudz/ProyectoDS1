package maker.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ProductoBD {

    private Connection connection;

    public ProductoBD() {
        connection = new ConnectionFactory().getConnection();
    }

    public boolean registrar(Producto producto){

        String sql = "INSERT INTO public.Producto (codRefProd, codigoProv, tipoFormato, nomArtista, album, generoAl, costoProd, precioVentaProd, cantidadStock)" +
                " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        return statementSQL(producto,sql);
    }

    public boolean actualizar(Producto producto){

        String sql = "UPDATE public.Producto SET codigoProv=?, tipoFormato=?, nomArtista=?, album=?, generoAl=?, costoProd=?, precioVentaProd=?, cantidadStock=?" +
                " WHERE codRefProd=?";

        return statementSQLUPDATE(producto,sql);
    }


    public ObservableList<Producto> mostrartable(){

        ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

        String sql = "SELECT codrefprod, codigoprov, tipoformato, nomartista, album, generoal, costoprod, precioventaprod, cantidadstock FROM ds1.public.producto";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String codigo = resultSet.getString("codRefProd");
                String proveedor = resultSet.getString("codigoProv");
                String tipo = resultSet.getString("tipoFormato");
                String artista = resultSet.getString("nomArtista");
                String album = resultSet.getString("album");
                String genero = resultSet.getString("generoAl");
                String coste = resultSet.getString("costoProd");
                String precioVenta = resultSet.getString("precioVentaProd");
                String stock = resultSet.getString("cantidadStock");

                Producto producto = new Producto(codigo, proveedor, tipo, artista, album, genero, coste, precioVenta, stock);

                listaProductos.add(producto);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listaProductos;
    }



    public void inactivar(String identificacion){

        String sql = "DELETE FROM ds1.public.Producto WHERE codRefProd=?";

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

    public Producto consultar(String identificacion){

        ObservableList<Producto> productos = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ds1.public.Producto WHERE codRefProd like '%" + identificacion + "%' ";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String codigo = resultSet.getString("codRefProd");
                String proveedor = resultSet.getString("codigoProv");
                String tipo = resultSet.getString("tipoFormato");
                String artista = resultSet.getString("nomArtista");
                String album = resultSet.getString("album");
                String genero = resultSet.getString("generoAl");
                String coste = resultSet.getString("costoProd");
                String precioVenta = resultSet.getString("precioVentaProd");
                String stock = resultSet.getString("cantidadStock");

                Producto producto = new Producto(codigo, proveedor, tipo, artista, album, genero, coste, precioVenta, stock);

                return producto;

            }
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        System.out.println("se retorna el producto null");
        return null;
    }

    //se acciona de acuerdo al sql.
    public boolean statementSQL(Producto producto, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, producto.getCodigo());
            statement.setString(2, producto.getUnProveedor());
            statement.setString(3, producto.getTipo());
            statement.setObject(4, producto.getArtista());
            statement.setString(5, producto.getAlbum());
            statement.setString(6, producto.getGenero());
            statement.setString(7, producto.getCoste());
            statement.setString(8, producto.getPrecioVenta());
            statement.setString(9, producto.getCantidad());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public boolean statementSQLUPDATE(Producto producto, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, producto.getUnProveedor());
            statement.setString(2, producto.getTipo());
            statement.setObject(3, producto.getArtista());
            statement.setString(4, producto.getAlbum());
            statement.setString(5, producto.getGenero());
            statement.setString(6, producto.getCoste());
            statement.setString(7, producto.getPrecioVenta());
            statement.setString(8, producto.getCantidad());
            statement.setString(9, producto.getCodigo());


            statement.execute();
            statement.close();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }
}