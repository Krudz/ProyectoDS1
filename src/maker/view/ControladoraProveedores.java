package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import maker.model.Proveedor;
import maker.model.ProveedorBD;

public class ControladoraProveedores {

    @FXML
    private TableView<Proveedor> tablaProveedores;
    @FXML
    private TableColumn<Proveedor,String> tvCodigo;
    @FXML
    private TableColumn<Proveedor,String> tvNombre;
    @FXML
    private TableColumn<Proveedor,String> tvNIT;
    @FXML
    private TableColumn<Proveedor,String> tvTelefono;
    @FXML
    private TableColumn<Proveedor,String> tvDireccion;

    private Stage stage;

    private ProveedorBD bd;

    //datos de prueba para productos que se agregan a la factura
    private ObservableList<Proveedor> listaProveedores = FXCollections.observableArrayList();

    public ObservableList<Proveedor> getListaProveedores() {
        return listaProveedores;
    }

    public void setListaProveedores(ObservableList<Proveedor> listaProveedores) {
        this.listaProveedores = listaProveedores;
    }

    //se crea objeto de tipo controladora ini que pasara los datos desde controladora inicio
    ///**********************************
    private ControladoraInicio controladoraIni;

    public void setControlIni(ControladoraInicio controladoraIni) {
        this.controladoraIni = controladoraIni;
    }
    ///**********************************

    public void setStage(Stage stage){
        this.stage = stage;
    }

    @FXML
    public void initialize(){

        bd = new ProveedorBD();

        //agregar tipos de datos que se ingresaran a las columnas de los items de proveedor
        tvCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        tvNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());
        tvNIT.setCellValueFactory(cellData -> cellData.getValue().nitProperty());
        tvTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        tvDireccion.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());

        actualizaListaProveedores();

        addToTabla();
    }


    @FXML
    public void editarProveedor(){
        Proveedor proveedorToEditar = tablaProveedores.getSelectionModel().getSelectedItem();
        if(proveedorToEditar != null){
            controladoraIni.setProveedorToEditar(proveedorToEditar);
            controladoraIni.mostrarVentanaAddProveedores();
        }

    }

    @FXML
    public void inactivarProveedor(){
        Proveedor proveedorToDelete = tablaProveedores.getSelectionModel().getSelectedItem();
        if(proveedorToDelete != null){
            bd.inactivar(proveedorToDelete.getCodigo());
            listaProveedores.remove(proveedorToDelete);
        }
    }


    @FXML
    public void addProveedor(){
        controladoraIni.setProveedorToEditar(null);
        controladoraIni.mostrarVentanaAddProveedores();
    }

    public void addToTabla(){
        tablaProveedores.setEditable(true);
        tablaProveedores.setItems(listaProveedores);
    }

    public void actualizaListaProveedores(){
        //trae informacion de la base de datos
        listaProveedores = bd.mostrartable();
    }
}
