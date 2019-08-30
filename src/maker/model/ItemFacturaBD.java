package maker.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;


public class ItemFacturaBD {

    private Connection connection;

    public ItemFacturaBD() {
        connection = new ConnectionFactory().getConnection();

    }

    public void registrar(ItemFactura item){

        String sql = "INSERT INTO ds1.public.itemfactura VALUES( '" + item.getNroRefFactura() + "'," + "'" + item.getCodRefProd() + "'," + "'" + item.getDescripcion() +
                "'," + "'" + item.getValorUnitario() + "'," + "'" + item.getCantidad() + "'," + "'" + item.getSubtotal() + "')";

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

        String sql = "DELETE FROM ds1.public.ItemFactura WHERE nroRefFactura=?";

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

    public ObservableList consultar(String nroRef){

        ObservableList<ItemFactura> listaItems = FXCollections.observableArrayList();

        String sql = "SELECT * FROM ds1.public.ItemFactura WHERE" +  "\"nroRefFactura\"" +  "like '" + nroRef + "' ";
        System.out.println(sql);

        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                String nroRefFactura = resultSet.getString("nroRefFactura");
                String codRefProd = resultSet.getString("codRefProd");
                String descripcion =  resultSet.getString("descripcion");
                String valorUnitario = resultSet.getString("valorUnitario");
                String cantidad = resultSet.getString("cantidad");
                String subtotal = resultSet.getString("subtotal");

                ItemFactura item = new ItemFactura(nroRefFactura,codRefProd,descripcion,valorUnitario,cantidad,subtotal);
                listaItems.add(item);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return listaItems;
    }


    //se acciona de acuerdo al sql.
    public void statementSQL(ItemFactura factura, String sql){
        try {
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, factura.getNroRefFactura());
            statement.setString(2, factura.getCodRefProd());
            statement.setString(3, factura.getDescripcion());
            statement.setString(4, factura.getValorUnitario());
            statement.setString(5, factura.getCantidad());
            statement.setString(6, factura.getSubtotal());

            statement.execute();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
