package maker.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class FacturaBD {

    private Connection connection;

    public FacturaBD() {
        connection = new ConnectionFactory().getConnection();

    }

    //para el consecutivo de la factura
    public int contarRegistros(){
        String sql = "SELECT count(*) FROM ds1.public.factura";
        int cantidad = -1;

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            while(resultSet.next()){
                cantidad = resultSet.getInt(1);
                System.out.println("la cantidad es " + cantidad);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return cantidad;
    }



    public void registrar(Factura factura){

        String sql = "INSERT INTO ds1.public.factura VALUES( '" + factura.getNumeroRef() + "'," + "'" + factura.getDatosCliente() + "'," + "'" + factura.getFechaDeVenta().toString() +
                "'," + "'" + factura.getMedioDePago() + "'," + "'" + factura.getTotalVenta() + "')";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.execute();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public void inactivar(String identificacion){

        String sql = "DELETE FROM ds1.public.factura WHERE nroRefFactura=?";

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

    public ObservableList<Factura> consultar(LocalDate fechaFacturaS){

        ObservableList<Factura> facturasPorDia = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ds1.public.Factura WHERE " + "\"fechaFactura\"" + " = '" + fechaFacturaS + "'";

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String numeroRef = resultSet.getString("nroRefFactura");
                String cliente = resultSet.getString("docIdCl");
                LocalDate fecha =  resultSet.getDate("fechaFactura").toLocalDate();
                String medioPago = resultSet.getString("medioPago");
                String total = resultSet.getString("totalFactura");

                Factura factura = new Factura(numeroRef,cliente,fecha,medioPago,total);

                facturasPorDia.add(factura);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return facturasPorDia;
    }





    //se acciona de acuerdo al sql.
    public void statementSQL(Factura factura, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, factura.getNumeroRef());
            statement.setString(2, factura.getDatosCliente());
            statement.setObject(3, factura.getFechaDeVenta());
            statement.setString(4, factura.getMedioDePago());
            statement.setString(5, factura.getTotalVenta());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
