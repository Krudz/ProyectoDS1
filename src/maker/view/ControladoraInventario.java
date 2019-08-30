package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import maker.model.Producto;
import maker.model.ProductoBD;


public class ControladoraInventario {

    @FXML
    private TableView<Producto> tablaInventario;
    @FXML
    private TableColumn<Producto, String> tvCodigo;
    @FXML
    private TableColumn<Producto, String> tvProducto;
    @FXML
    private TableColumn<Producto, String> tvStock;
    @FXML
    private TableColumn<Producto, String> tvPrecio;
    @FXML
    private TableColumn<Producto, String> tvCoste;


    private Stage stage;
    private ProductoBD bd;

    //datos de prueba para productos que se agregan a la factura
    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();

    public ObservableList<Producto> getListaProductos() {
        return listaProductos;
    }

    public void setListaProductos(ObservableList<Producto> listaProductos) {
        this.listaProductos = listaProductos;
    }
    //*************************


    //se crea objeto de tipo controladora ini que pasara los datos desde controladora inicio
    ///**********************************
    private ControladoraInicio controladoraIni;

    public void setControlIni(ControladoraInicio controladoraIni) {
        this.controladoraIni = controladoraIni;
    }
    ///**********************************


    public void setStage(Stage stage) {
        this.stage = stage;
    }




    @FXML
    public void initialize() {

        bd = new ProductoBD();


        //agregar tipos de datos que se ingresaran a las columnas de los items de producto
        tvCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        tvProducto.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        tvStock.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
        tvPrecio.setCellValueFactory(cellData -> cellData.getValue().precioVentaProperty());
        tvCoste.setCellValueFactory(cellData -> cellData.getValue().costeProperty());

        actualizaListaInventario();

        addToTabla();
    }


    @FXML
    public void editarProducto() {
        Producto productoToEditar = tablaInventario.getSelectionModel().getSelectedItem();
        if (productoToEditar != null) {
            controladoraIni.setProductoToEditar(productoToEditar);
            controladoraIni.mostrarVentanaAddInventario();
        }
    }

    @FXML
    public void addProducto() {
        controladoraIni.setProductoToEditar(null);
        controladoraIni.mostrarVentanaAddInventario();
    }

    @FXML
    public void inactivarProducto() {
        Producto productoToDelete = tablaInventario.getSelectionModel().getSelectedItem();
        if (productoToDelete != null) {
            bd.inactivar(productoToDelete.getCodigo());
            listaProductos.remove(productoToDelete);
        }
    }


    public void addToTabla() {
        tablaInventario.setEditable(true);
        tablaInventario.setItems(listaProductos);
    }

    public void actualizaListaInventario() {
        //trae informacion de la base de datos
        listaProductos = bd.mostrartable();
    }
}
