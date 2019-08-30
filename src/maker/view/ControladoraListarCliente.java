package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import maker.model.Cliente;
import maker.model.ClienteBD;

public class ControladoraListarCliente {

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TableColumn<Cliente,String> tvNombre;
    @FXML
    private TableColumn<Cliente,String> tvCedula;
    @FXML
    private TableColumn<Cliente,String> tvGenero;
    @FXML
    private TableColumn<Cliente,String> tvDomicilio;
    @FXML
    private TableColumn<Cliente,String> tvTelefono;
    @FXML
    private TableColumn<Cliente,String> tvEmail;


    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    //************************ se crea obj de tipo controladora inicio
    private ControladoraInicio controladoraIni;

    public ControladoraInicio getControladoraIni() {
        return controladoraIni;
    }

    public void setControladoraIni(ControladoraInicio controladoraIni) {
        this.controladoraIni = controladoraIni;
    }
    //************************


    //************************ LISTA DE CLIENTES GUARDADOS
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();


    private ClienteBD bd;

    public void setListaClientes(ObservableList<Cliente> listaClientes) {

        this.listaClientes.addAll(listaClientes);
        for (int i = 0; i < this.listaClientes.size(); i++) {
            System.out.println(this.listaClientes.get(i).getNombre());
        }
    }
    //************************


    @FXML
    public void initialize(){

        bd = new ClienteBD();


        //agregar tipos de datos que se ingresaran a las columnas de los items de producto

        tvNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCl"));
        tvCedula.setCellValueFactory(new PropertyValueFactory<>("docIdCl"));
        tvGenero.setCellValueFactory(new PropertyValueFactory<>("generoCl"));
        tvDomicilio.setCellValueFactory(new PropertyValueFactory<>("direcccionCl"));
        tvTelefono.setCellValueFactory(new PropertyValueFactory<>("telefonoCl"));
        tvEmail.setCellValueFactory(new PropertyValueFactory<>("emailCl"));

        tvNombre.setCellValueFactory(cellData -> cellData.getValue().nombreProperty().concat(" " + cellData.getValue().apellidoProperty().getValue()));
        tvCedula.setCellValueFactory(cellData -> cellData.getValue().identificacionProperty());
        tvGenero.setCellValueFactory(cellData -> cellData.getValue().generoProperty());
        tvDomicilio.setCellValueFactory(cellData -> cellData.getValue().domicilioProperty());
        tvTelefono.setCellValueFactory(cellData -> cellData.getValue().telefonoProperty());
        tvEmail.setCellValueFactory(cellData -> cellData.getValue().emailProperty());

        tablaClientes.setEditable(true);
        tablaClientes.setItems(listaClientes);
    }


    @FXML
    public void editarCliente(){
        Cliente clienteToEditar = tablaClientes.getSelectionModel().getSelectedItem();
        if(clienteToEditar != null){
            controladoraIni.setClienteToEditar(clienteToEditar);
            controladoraIni.setIngresoFromListarCliente(true);
            controladoraIni.setRunningProcess(true);
            controladoraIni.mostrarPanelClientes();
        }
    }


    @FXML
    public void borrarCliente(){
        Cliente clienteToBorrar = tablaClientes.getSelectionModel().getSelectedItem();
        bd.inactivar(clienteToBorrar.getIdentificacion());
        listaClientes.remove(clienteToBorrar);
    }


    @FXML
    public void regresarToCliente(){
        System.out.println("se regresa a formulario para ingresar clientes");
        controladoraIni.setRunningProcess(false);
        controladoraIni.setIngresoFromListarCliente(true);
        controladoraIni.setClienteToEditar(null);
        controladoraIni.mostrarPanelClientes();
    }


}
