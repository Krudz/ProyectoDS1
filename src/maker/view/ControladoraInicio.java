package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import maker.MainApp;
import maker.model.Cliente;
import maker.model.Producto;
import maker.model.Proveedor;
import maker.model.Usuario;

import java.io.IOException;

public class ControladoraInicio {

    @FXML
    private AnchorPane splitRight;
    @FXML
    private SplitPane splitAbs;
    @FXML
    private Label lUsuarioLogueado;


    public Label getlUsuarioLogueado() {
        return lUsuarioLogueado;
    }

    //**************establece un ingreso para cliente desde facturacion
    private boolean ingresoFromFacturacion = false;

    public boolean isIngresoFromFacturacion() {
        return ingresoFromFacturacion;
    }

    public void setIngresoFromFacturacion(boolean ingresoFromFacturacion) {
        this.ingresoFromFacturacion = ingresoFromFacturacion;
    }
    //**************

    //**************establece un ingreso para cliente desde listarcliente
    private boolean ingresoFromListarCliente = false;

    public boolean isIngresoFromListarCliente() {
        return ingresoFromListarCliente;
    }

    public void setIngresoFromListarCliente(boolean ingresoFromListarCliente) {
        this.ingresoFromListarCliente = ingresoFromListarCliente;
    }
    //**************

    //establecer control sobre el proceso que se este realizando
    //*******************************************
    private boolean runningProcess = false;

    public boolean isRunningProcess() {
        return runningProcess;
    }

    public void setRunningProcess(boolean runningProcess) {
        this.runningProcess = runningProcess;
    }
    //*******************************************


    //creacion de observablelist que me ira mostrando a medida que vaya agregando
    private ObservableList<Producto> listaProductosAddingToFacturacion = FXCollections.observableArrayList();


    //creacion de observable list para ir mostrando el cliente que se añade
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();

    public void setListaClientes(ObservableList<Cliente> listaClientes) {
        this.listaClientes = listaClientes;
    }
    //*******************


    //creacion de objeto cliente para cada nuevo cliente que se va creando
    //*******************
    private Cliente nuevoCliente;

    public void setNuevoCliente(Cliente nuevoCliente) {
        this.nuevoCliente = nuevoCliente;
    }
    //*******************


    //*******************objeto facturacion
    ControladoraFacturacion objFact = null;

    public ControladoraFacturacion getObjFact() {
        return objFact;
    }

    public void setObjFact(ControladoraFacturacion objFact) {
        this.objFact = objFact;
    }
    //**************************


    //*******************objeto cliente para editar
    Cliente clienteToEditar;


    public void setClienteToEditar(Cliente clienteToEditar) {
        this.clienteToEditar = clienteToEditar;
    }
    //*******************

    //*******************objeto proveedor para editar
    Proveedor proveedorToEditar;

    public void setProveedorToEditar(Proveedor proveedorToEditar) {
        this.proveedorToEditar = proveedorToEditar;
    }
    //*******************

    //*******************Producto obj a editar
    Producto productoToEditar;


    public void setProductoToEditar(Producto productoToEditar) {
        this.productoToEditar = productoToEditar;
    }
    //*******************

    //*******************objeto main
    //se pasa el objeto de tipo MainApp
    public void setMainApp(MainApp mainApp){
        this.mainApp = mainApp;
    }

    public MainApp getMainApp() {
        return mainApp;
    }
    //***************************


    //*******************objeto usuario encontrado
    private Usuario usuarioLogueado;

    public void setUsuarioLogueado(Usuario usuarioLogueado){
        this.usuarioLogueado = usuarioLogueado;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }
    //*******************

    private Stage inicioStage;
    private MainApp mainApp;
    private AnchorPane panelLayout;


    @FXML
    private void initialize(){

    }

    public void setInicioStage(Stage inicioStage){
        this.inicioStage = inicioStage;
    }


    @FXML
    public void cerrarSesion(){
        mainApp.loginLayout();
    }


    @FXML
    public void mostrarPanelFacturacion() {
        try {
            System.out.println("running process esta: " + isRunningProcess());
            //se carga contenido de facturacion a splitRight
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/facturacion.fxml"));
                splitRight = (AnchorPane) loader.load();

                ControladoraFacturacion ctrlFacturacion;
                ctrlFacturacion = loader.getController();

                ctrlFacturacion.setControlIni(this);

                ctrlFacturacion.mostrarDatosTablaProductos(listaProductosAddingToFacturacion, nuevoCliente);


                //se añade el contenido de facturacion al split inferior
                splitAbs.getItems().set(1, splitRight);

                //limpia campos luego de recepcionar el cambio de estado de tarea
                if (!isRunningProcess()) {
                    ctrlFacturacion.limpiarCamposFacturacion();
                    System.out.println("se limpian campos de facturacion");
                }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void mostrarPaneladdProductoFacturacion() {
        try {
            //se carga contenido de facturacion a splitRight
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/addProducto-facturacion.fxml"));
            splitRight = (AnchorPane) loader.load();


            //se pasa el objeto de la clase inicio.
            ControladoraAddProductoFacturacion ctrlAddProductoFac = loader.getController();
            ctrlAddProductoFac.setControlIni(this);


            //se añade el contenido de facturacion al split inferior
            splitAbs.getItems().set(1,splitRight);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //se carga el objeto pasado por la clase de seleccion de producto para facturacion
    public void addProductoSeleccionadoToFactura(Producto productoSeleccionado){
        listaProductosAddingToFacturacion.add(productoSeleccionado);
    }


    @FXML
    public void mostrarPanelClientes(){
        try {
            if(!isRunningProcess() || isIngresoFromListarCliente()) {
                System.out.println("running process esta: " + isRunningProcess());
                //se carga contenido de clientes a splitRight
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/cliente.fxml"));
                splitRight = (AnchorPane) loader.load();

                ControladoraCliente controladoraCli;
                controladoraCli = loader.getController();

                controladoraCli.setControladoraIni(this);

                if(isIngresoFromFacturacion()){
                    setRunningProcess(true);
                }

                if(clienteToEditar != null){
                    controladoraCli.editarCliente(clienteToEditar);
                }


                //limpia campos luego de recepcionar el cambio de estado de tarea
                if (!isRunningProcess()) {
                    controladoraCli.limpiarCampos();
                    System.out.println("se limpian campos");
                }

                //se añade el contenido de clientes al split inferior
                splitAbs.getItems().set(1, splitRight);
            }
            else{
               mostrarVentanaRunningProcess();
           }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void mostrarPanelListarClientes() {
        try {
            //se carga contenido de facturacion a splitRight
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/listarcliente.fxml"));
            splitRight = (AnchorPane) loader.load();


            //se pasa el objeto de la clase inicio.
            ControladoraListarCliente ctrlListarClie = loader.getController();
            ctrlListarClie.setControladoraIni(this);

            ctrlListarClie.setListaClientes(listaClientes);


            //se añade el contenido de facturacion al split inferior
            splitAbs.getItems().set(1,splitRight);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void mostrarPanelPerfiles(){
        try {
            if(usuarioLogueado.getCargo().equalsIgnoreCase("Administrador")) {
                if (!isRunningProcess()) {
                    //se carga contenido de perfiles a splitRight
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/perfil.fxml"));
                    splitRight = (AnchorPane) loader.load();

                    //se pasa el objeto de la clase inicio.
                    ControladoraPerfil ctrlPerfil = loader.getController();
                    ctrlPerfil.setControladoraIni(this);

                    //se añade el contenido de perfiles al split inferior
                    splitAbs.getItems().set(1, splitRight);

                } else {
                    mostrarVentanaRunningProcess();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void mostrarPanelProveedores(){
        try {
            if(usuarioLogueado.getCargo().equalsIgnoreCase("Administrador")){
                if(!isRunningProcess()) {
                    //se carga contenido de proveedores a splitRight
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/proveedores.fxml"));
                    splitRight = (AnchorPane) loader.load();

                    //se pasa el objeto de la clase inicio.
                    ControladoraProveedores ctrlProveedor = loader.getController();
                    ctrlProveedor.setControlIni(this);

                    //se añade el contenido de proveedores al split inferior
                    splitAbs.getItems().set(1, splitRight);
                }
                else{
                    mostrarVentanaRunningProcess();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void mostrarVentanaAddProveedores(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/proveedorAdd.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //se crea el stage de dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir/Editar Proveedor");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //colocar persona en el controlador
            ControladoraProveedorAdd controller = loader.getController();

            //se pasa objeto de tipo controlador inicio
            controller.setControladoraIni(this);

            if(proveedorToEditar != null){
                System.out.println("se va a editar");
                controller.mostrarDatos(proveedorToEditar);
            }

            //pasa el stage para mostrar la ventana emergente
            controller.setStage(dialogStage);
            dialogStage.showAndWait();


        }catch (IOException ie){
            ie.printStackTrace();
        }
    }


    @FXML
    public void mostrarPanelInventario(){
        try {
            if(usuarioLogueado.getCargo().equalsIgnoreCase("Administrador")) {
                if (!isRunningProcess()) {
                    //se carga contenido de inventario a splitRight
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/inventario.fxml"));
                    splitRight = (AnchorPane) loader.load();

                    //se pasa el objeto de la clase inicio.
                    ControladoraInventario ctrlInventario = loader.getController();
                    ctrlInventario.setControlIni(this);


                    //se añade el contenido de inventario al split inferior
                    splitAbs.getItems().set(1, splitRight);
                } else {
                    System.out.println("mensaje de obstruccion para finalizar el proceso en ejecucion");
                    mostrarVentanaRunningProcess();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void mostrarVentanaAddInventario(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/inventarioAddProducto.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //se crea el stage de dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Añadir/Editar Inventario");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //colocar persona en el controlador
            ControladoraInventarioAddProducto controller = loader.getController();

            //se pasa objeto de tipo controlador inicio
            controller.setControladoraIni(this);

            if(productoToEditar != null){
                controller.mostrarDatos(productoToEditar);
            }

            //pasa el stage para mostrar la ventana emergente
            controller.setStage(dialogStage);
            dialogStage.showAndWait();


        }catch (IOException ie){
            ie.printStackTrace();
        }
    }


    @FXML
    public void mostrarPanelReportes(){
        try {
            if(usuarioLogueado.getCargo().equalsIgnoreCase("Administrador")) {
                if (!isRunningProcess()) {
                    //se carga contenido de reporte a splitRight
                    FXMLLoader loader = new FXMLLoader();
                    loader.setLocation(MainApp.class.getResource("view/reporte.fxml"));
                    splitRight = (AnchorPane) loader.load();

                    //se añade el contenido de reporte al split inferior
                    splitAbs.getItems().set(1, splitRight);
                } else {
                    System.out.println("mensaje de obstruccion para finalizar el proceso en ejecucion");
                    mostrarVentanaRunningProcess();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void mostrarPanelSoporte(){
        try {
            if(!isRunningProcess()) {
                //se carga contenido de soporte a splitRight
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(MainApp.class.getResource("view/soporte.fxml"));
                splitRight = (AnchorPane) loader.load();

                //se añade el contenido de soporte al split inferior
                splitAbs.getItems().set(1, splitRight);
            }
            else{
                System.out.println("mensaje de obstruccion para finalizar el proceso en ejecucion");
                mostrarVentanaRunningProcess();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //cuando se acciona una ventana de error por proceso que actualmente se esta ejecutando, se llama a la ventana emergente
    @FXML
    public void mostrarVentanaRunningProcess(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/runningProcess.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //se crea el stage de dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tarea en ejecución");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //colocar persona en el controlador
            ControladoraRunningProcess controller = loader.getController();

            //se pasa objeto de tipo controlador inicio
            controller.setControladoraIni(this);

            //pasa el stage para mostrar la ventana emergente
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();


        }catch (IOException ie){
            ie.printStackTrace();
        }
    }


    //cuando se acciona aceptar success, se llama a la ventana emergente
    @FXML
    public void mostrarVentanaExitosa(){
        try{
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/exitoMessage.fxml"));
            AnchorPane page = (AnchorPane) loader.load();

            //se crea el stage de dialog
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Operación Satisfactoria");
            dialogStage.initModality(Modality.APPLICATION_MODAL);
            dialogStage.initOwner(mainApp.getPrimaryStage());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            //colocar persona en el controlador
            ControladoraExitoMessage controller = loader.getController();

            //se pasa objeto de tipo controlador inicio
            controller.setControladoraIni(this);

            //pasa el stage para mostrar la ventana emergente
            controller.setDialogStage(dialogStage);
            dialogStage.showAndWait();


        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
}
