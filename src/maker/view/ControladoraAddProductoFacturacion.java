package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import maker.model.*;

public class ControladoraAddProductoFacturacion {

    @FXML
    private TableView<Producto> tablaProductosToAdd;
    @FXML
    private TableColumn<Producto, String> tvCodigo;
    @FXML
    private TableColumn<Producto, String> tvProducto;
    @FXML
    private TableColumn<Producto, String> tvStock;
    @FXML
    private TableColumn<Producto, String> tvCoste;
    @FXML
    private TableColumn<Producto, String> tvPrecioVenta;


    private ObservableList<Producto> listaProductos = FXCollections.observableArrayList();


    private Stage stage;

    private ControladoraInicio controladoraIni;


    private ProductoBD bd;

    public void setControlIni(ControladoraInicio controladoraIni) {
        this.controladoraIni = controladoraIni;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }


    @FXML
    public void initialize() {

        bd = new ProductoBD();

        listaProductos = bd.mostrartable();

        //se cargan los datos a la tabla
        //agregar tipos de datos que se ingresaran a las columnas de los items de producto
        tvCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        tvProducto.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        tvStock.setCellValueFactory(cellData -> cellData.getValue().cantidadProperty());
        tvCoste.setCellValueFactory(cellData -> cellData.getValue().costeProperty());
        tvPrecioVenta.setCellValueFactory(cellData -> cellData.getValue().precioVentaProperty());

        tablaProductosToAdd.setEditable(true);
        tablaProductosToAdd.setItems(listaProductos);
    }

    //METODO PARA INVOCAR EL RETORNO A LA PAGINA PRINCIPAL DE FACTURACION
    //en cuanto se de clic en regresar sin seleccionar nada
    @FXML
    public void regresarAFacturacion() {
        System.out.println("se esta regresando a la ventana ppal de facturacion, sin seleccionar");
        controladoraIni.mostrarPanelFacturacion();
    }


    //CUANDO SE SELECCIONA AÑADIR ITEM
    @FXML
    public void addProductoToFactura() {
        System.out.println("se esta regresando a la ventana ppal de facturacion con un objeto ya seleccionado");


        //producto seleccionado en tabla
        Producto productoToAdd = tablaProductosToAdd.getSelectionModel().getSelectedItem();

        if (productoToAdd != null) {
            //pasar el objeto para la controladora inicio
            controladoraIni.addProductoSeleccionadoToFactura(productoToAdd);

            String nroRefFactura = tvCodigo.getTableView().getSelectionModel().getSelectedItem().getCodigo();
            String codRefProd = tvCodigo.getTableView().getSelectionModel().getSelectedItem().getCodigo();
            String descripcion = tvProducto.getTableView().getSelectionModel().getSelectedItem().getDescripcion();
            String valorUnitario = tvCoste.getTableView().getSelectionModel().getSelectedItem().getCoste();
            String cantidad = tvStock.getTableView().getSelectionModel().getSelectedItem().getCantidad();
            String subtotal = tvPrecioVenta.getTableView().getSelectionModel().getSelectedItem().getPrecioVenta();

            ItemFactura itemfactura = new ItemFactura(nroRefFactura, codRefProd, descripcion, valorUnitario, cantidad, subtotal);

            System.out.println("se añadira un item a la factura");

        } else {
            System.out.println("no se añadio ningun producto");
        }


        controladoraIni.mostrarPanelFacturacion();

    }


}
