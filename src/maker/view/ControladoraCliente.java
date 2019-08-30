package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import maker.model.Cliente;
import maker.model.ClienteBD;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ControladoraCliente {

    @FXML
    private TableView<Cliente> tablaClientes;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellido;
    @FXML
    private TextField tfCedula;
    @FXML
    private ComboBox cbGenero;
    @FXML
    private TextField tfDomicilio;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfEmail;
    @FXML
    private DatePicker dpSelectorFecha;



    //variable para fecha, asignada por defecto
    LocalDate fechaInscripcion = LocalDate.now();


    private Stage stage;

    public void setStage(Stage stage) {
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
    //************************


    //***********
    private Cliente clienteToEditar;

    public void editarCliente(Cliente clienteToEditar) {
        this.clienteToEditar = clienteToEditar;
        completarCamposForEditar();
    }
    //************************


    @FXML
    public void initialize() {

        bd = new ClienteBD();

        //coloca valores por defecto en genero
        cbGenero.getItems().add(0, "M");
        cbGenero.getItems().add(1, "F");

        actualizaListaClientes();
    }


    public void completarCamposForEditar() {
        try {
            // el que parsea
            SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
            // el que formatea
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

            tfNombre.setText(clienteToEditar.getNombre());
            tfApellido.setText(clienteToEditar.getApellido());
            tfCedula.setText(clienteToEditar.getIdentificacion());
            dpSelectorFecha.getEditor().setText(formateador.format(parseador.parse(clienteToEditar.getFechaNacimiento().toString())));
            cbGenero.getSelectionModel().selectFirst();
            tfDomicilio.setText(clienteToEditar.getDomicilio());
            tfTelefono.setText(clienteToEditar.getTelefono());
            tfEmail.setText(clienteToEditar.getEmail());

        } catch (Exception pe) {
            pe.printStackTrace();
        }
    }


    @FXML
    public void addCliente() {
        if (validarCamposVacios()) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

            String identificacion = tfCedula.getText();
            String nombre = tfNombre.getText();
            String apellido = tfApellido.getText();
            LocalDate fechaNacimiento = LocalDate.parse(dpSelectorFecha.getEditor().getText(), formatter);
            String genero = cbGenero.getSelectionModel().getSelectedItem().toString();
            String domicilio = tfDomicilio.getText();
            String telefono = tfTelefono.getText();
            String email = tfEmail.getText();

            Cliente nuevoCliente = new Cliente(identificacion, nombre, apellido, fechaNacimiento, genero,
                    domicilio, telefono, email);

            if (!controladoraIni.isIngresoFromListarCliente()) {
                bd.registrar(nuevoCliente);
                listaClientes.add(nuevoCliente);
                System.out.println("agregad nuevo cliente");
            } else {
                bd.actualizar(nuevoCliente);
                controladoraIni.setIngresoFromListarCliente(false);
                actualizaListaClientes();
            }

            System.out.println("added to bd");
            limpiarCampos();

            controladoraIni.setNuevoCliente(nuevoCliente);

            //si viene de facturacion, se devolvera para continuar la misma.
            if (controladoraIni.isIngresoFromFacturacion()) {
                controladoraIni.mostrarPanelFacturacion();
            }
        }
    }


    @FXML
    public void modificarListaCliente() {

        if (!controladoraIni.isIngresoFromFacturacion()) {
            System.out.println("me llevara a la ventana de visualizacion de todos los clientes");
            controladoraIni.setListaClientes(listaClientes);
            controladoraIni.mostrarPanelListarClientes();

        }
    }


    @FXML
    public void regresarFacturacion() {
        //valida si viene desde facturacion o se aplica directamente en la ventana.
        if (controladoraIni.isIngresoFromFacturacion()) {
            System.out.println("se esta regresando a la ventana ppal de facturacion, sin añadir cliente");
            controladoraIni.mostrarPanelFacturacion();
        }

    }


    public boolean validarCamposVacios() {
        if (tfApellido.getText().isEmpty() || tfNombre.getText().isEmpty() || tfCedula.getText().isEmpty() ||
                cbGenero.getSelectionModel().getSelectedIndex() < 0 || tfDomicilio.getText().isEmpty() || tfTelefono.getText().isEmpty() ||
                tfEmail.getText().isEmpty() || dpSelectorFecha.getEditor().getText().equals("".trim()) || dpSelectorFecha.getEditor().getText().equals("Seleccionar".trim())) {
            System.out.println("error de datos, falta completar");
            return false;
        }
        return true;
    }


    public void limpiarCampos() {
        tfNombre.setText("");
        tfApellido.setText("");
        tfCedula.setText("");
        cbGenero.getSelectionModel().clearSelection();
        tfDomicilio.setText("");
        tfTelefono.setText("");
        tfEmail.setText("");
        dpSelectorFecha.getEditor().setText("Seleccionar");
    }

    public void actualizaListaClientes() {
        //trae informacion de la base de datos
        listaClientes = bd.mostrartable();
    }

}
