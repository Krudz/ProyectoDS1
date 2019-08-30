package maker.view;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import maker.model.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class ControladoraFacturacion {

    private Stage stage;
    @FXML
    private AnchorPane panelFacturacion;
    @FXML
    private Label numeracionFactura;
    @FXML
    private DatePicker selectorFecha;
    @FXML
    private Label nombreCliente;
    @FXML
    private Label telefonoCliente;
    @FXML
    private Label ccCliente;
    @FXML
    private Label emailCliente;
    @FXML
    private ComboBox cbSelectorPagoFactura;
    @FXML
    private ComboBox cbSelectorClientes;
    @FXML
    private TableView<Producto> tablaProductosFactura;
    @FXML
    private TableColumn<Producto, String> tvCodigo;
    @FXML
    private TableColumn<Producto, String> tvDescripcion;
    @FXML
    private TableColumn<Producto, String> tvSubtotal;
    @FXML
    private Label lValorTotal;
    @FXML
    private Label direccionCliente;


    //productos que se agregan a la factura
    private ObservableList<Producto> listaproductosAddedToFactura = FXCollections.observableArrayList();

    private ObservableList<Cliente> listaClientesActuales = FXCollections.observableArrayList();


    //se crea objeto de tipo controladora ini que pasara los datos desde controladora inicio
    ///**********************************
    private ControladoraInicio controladoraIni;

    public void setControlIni(ControladoraInicio controladoraIni) {
        this.controladoraIni = controladoraIni;
    }
    ///**********************************


    private boolean isClienteSelected = false;
    private Cliente datosClienteFacturacion;

    public Cliente getDatosClienteFacturacion() {
        return datosClienteFacturacion;
    }

    public void setDatosClienteFacturacion(Cliente datosClienteFacturacion) {
        this.datosClienteFacturacion = datosClienteFacturacion;
    }
    ///**********************************

    //variable para fecha, asignada por defecto
    LocalDate fechaFactura = LocalDate.now();


    public void setStage(Stage stage) {
        this.stage = stage;
    }


    private FacturaBD bdFactura;
    private ClienteBD bdCliente;
    private ItemFacturaBD bdItemFactura;

    public int fijarConsecutivo() {
        //revisar el ultimo valor dado al consecutivo de la factura
        return bdFactura.contarRegistros() + 1;
    }


    @FXML
    public void initialize() {

        bdFactura = new FacturaBD();
        bdCliente = new ClienteBD();
        bdItemFactura = new ItemFacturaBD();


        numeracionFactura.setText("000" + fijarConsecutivo());

        //inicializar forma de pago
        cbSelectorPagoFactura.getItems().add(0, "Tarjeta");
        cbSelectorPagoFactura.getItems().add(1, "Efectivo");


        listaClientesActuales = bdCliente.mostrartable();

        //inicializar lista de clientes traida de la base de datos
        for (int i = 0; i < listaClientesActuales.size(); i++) {
            cbSelectorClientes.getItems().add(i, listaClientesActuales.get(i).getNombre() + " " + listaClientesActuales.get(i).getApellido());
        }

        //agregar tipos de datos que se ingresaran a las columnas de los items de producto
        tvCodigo.setCellValueFactory(cellData -> cellData.getValue().codigoProperty());
        tvDescripcion.setCellValueFactory(cellData -> cellData.getValue().descripcionProperty());
        tvSubtotal.setCellValueFactory(cellData -> cellData.getValue().precioVentaProperty());
    }


    //a traves del objeto controladora inicio se usa el metodo para mostrar el contenido del panel
    @FXML
    public void mostrarAddProductoFacturacion() {
        revisarFecha();
        controladoraIni.setObjFact(this);
        controladoraIni.mostrarPaneladdProductoFacturacion();

        //se activa runningprocess
        controladoraIni.setRunningProcess(true);
    }


    //permite revisar la fecha seleccionada por el usuario.
    public boolean revisarFecha() {

        if (fechaFactura == LocalDate.now()) {
            fechaFactura = selectorFecha.getValue();
            System.out.println("si son diferentes las fechas");
        }

        if (fechaFactura != null) {
            if (fechaFactura.toString().trim().equals(LocalDate.now().toString().trim())) {
                System.out.println("la fecha es correcta, ahora se adjunta a los datos de la factura");
                return true;
            } else {
                System.out.println("por favor ajuste la fecha actual");
                return false;
            }
        } else {
            System.out.println("debe seleccionar una fecha antes de generar factura");
            return false;
        }
    }


    //prueba,,,, para cuando se añada un cliente, este se pasara el objeto y se muestra en los respectivos labels
    public void mostrarDatosTablaProductos(ObservableList<Producto> listaProductosAddingToFacturacion, Cliente nuevoCliente) {
        try {
            // el que parsea
            SimpleDateFormat parseador = new SimpleDateFormat("yyyy-MM-dd");
            // el que formatea
            SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");

            //prueba con paso de objeto para obtener fecha
            if (controladoraIni.getObjFact() != null) {
                fechaFactura = LocalDate.now();
                Date date = parseador.parse(fechaFactura.toString());
                selectorFecha.getEditor().setText(formateador.format(date));

                if (fechaFactura != null) {
                    selectorFecha.getEditor().setText(formateador.format(date));
                }

                //se coloca la anterior seleccion de tipo de pago -efectivo - tarjeta
                int posSelectorPago = controladoraIni.getObjFact().cbSelectorPagoFactura.getSelectionModel().getSelectedIndex();
                System.out.println("la pos pago es: " + posSelectorPago);
                cbSelectorPagoFactura.getSelectionModel().select(posSelectorPago);


                //se coloca la anterior seleccion de cliente para que coincida con el que esta en los campos
                int posSelectorCliente = controladoraIni.getObjFact().cbSelectorClientes.getSelectionModel().getSelectedIndex();
                System.out.println("la posicion cliente es:" + posSelectorCliente);
                cbSelectorClientes.getSelectionModel().select(posSelectorCliente);


                //verifica si el cliente que se va a añadir es nuevo
                if (nuevoCliente == null) {
                    System.out.println("ingresa al nulo nuevo cliente");
                    System.out.println("tamaño de datoscliente: " + listaClientesActuales.size());
                    //para pasar cliente por objeto
                    datosClienteFacturacion = controladoraIni.objFact.datosClienteFacturacion;

                    //revisa que los datos del cliente no esten vacios
                    if (datosClienteFacturacion != null) {
                        colocarDatosCliente();
                    }
                } else {
                    System.out.println("ingresa al no nulo nuevo cliente");
                    datosClienteFacturacion = nuevoCliente;
                    colocarDatosCliente();
                    cbSelectorClientes.getSelectionModel().select(cbSelectorClientes.getItems().size() - 1);
                    System.out.println("tamaño de datoscliente: " + listaClientesActuales.size());
                }
            }


            //recibe la lista de los productos que se van añadiendo
            if (listaProductosAddingToFacturacion != null) {
                for (int i = 0; i < listaProductosAddingToFacturacion.size(); i++) {
                    System.out.println(listaProductosAddingToFacturacion.get(i).getArtista() + "\n");
                }

                listaproductosAddedToFactura = listaProductosAddingToFacturacion;
                tablaProductosFactura.setEditable(true);
                tablaProductosFactura.setItems(listaproductosAddedToFactura);

                addValorTotalProgresivo();
            }


        } catch (ParseException pe) {
            System.out.println(pe.getMessage());
        }
    }


    //se suma a medida que se vaya añadiendo un nuevo item en la tabla
    public void addValorTotalProgresivo() {
        int subtotal = 0;
        String valorAconvertir = "";
        for (int i = 0; i < tablaProductosFactura.getItems().size(); i++) {
            valorAconvertir = tablaProductosFactura.getItems().get(i).getPrecioVenta();
            subtotal += Integer.parseInt(valorAconvertir);
        }

        lValorTotal.setText("" + subtotal);
        System.out.println("este es el valor a pagar: " + subtotal);
    }


    //para ir añadiendo los datos de los clientes a los label
    @FXML
    public void seleccionadoCliente() {
        try {
            String nombre = cbSelectorClientes.getSelectionModel().getSelectedItem().toString();
            System.out.println("se ha seleccionado un cliente" + nombre);
            datosClienteFacturacion = buscarDatos(nombre.trim());
            isClienteSelected = true;

            //se activa runningprocess
            controladoraIni.setRunningProcess(true);

            if (datosClienteFacturacion != null) {
                colocarDatosCliente();
                //si llego a existir un cliente nuevo asignado, se debe nulear para que no lo vuelva a traer
                controladoraIni.setNuevoCliente(null);
            } else {
                System.out.println("no encontrado");
            }
        } catch (Exception e) {
            System.out.println("seleccion cero");
        }

    }


    //metodo para buscar en el arraylist el cliente determinado
    public Cliente buscarDatos(String nombreCliente) {
        for (int i = 0; i < listaClientesActuales.size(); i++) {
            if (nombreCliente.equals(listaClientesActuales.get(i).getNombre() + " " + listaClientesActuales.get(i).getApellido())) {
                return listaClientesActuales.get(i);
            }
        }
        return null;
    }


    //quitar producto de la lista de facturas
    public void quitarProductoFactura() {
        int pos = tablaProductosFactura.getSelectionModel().getFocusedIndex();
        if (pos >= 0) {
            tablaProductosFactura.getItems().remove(pos);
            System.out.println("item eliminado satisfactoriamente");

            //para que actualice el valor total
            addValorTotalProgresivo();

            //prueba para mostrar lo que borra
            for (int i = 0; i < listaproductosAddedToFactura.size(); i++) {
                System.out.println(listaproductosAddedToFactura.get(i).getArtista() + "\n");
            }
        } else {
            System.out.println("no se hay items para borrar");
        }
    }


    //colocar los datos del cliente
    private void colocarDatosCliente() {
        nombreCliente.setText(datosClienteFacturacion.getNombre());
        direccionCliente.setText(datosClienteFacturacion.getDomicilio());
        telefonoCliente.setText(datosClienteFacturacion.getTelefono());
        ccCliente.setText("" + datosClienteFacturacion.getIdentificacion());
        emailCliente.setText(datosClienteFacturacion.getEmail());
    }


    //cuando se presione el boton generarFactura se inicia una verificacion previa para luego enviar datos.
    @FXML
    public void generarFactura() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        //revisar fecha
        if (revisarFecha() && cbSelectorPagoFactura.getSelectionModel().getSelectedIndex() >= 0
                && cbSelectorClientes.getSelectionModel().getSelectedIndex() >= 0 && tablaProductosFactura.getItems().size() >= 0) {

            String numeroRef = numeracionFactura.getText();
            String cliente = ccCliente.getText().trim();
            LocalDate fecha = LocalDate.parse(selectorFecha.getEditor().getText(), formatter);
            String medioPago = cbSelectorPagoFactura.getSelectionModel().getSelectedItem().toString();
            String total = lValorTotal.getText();


            Factura facturaData = new Factura(numeroRef, cliente, fecha, medioPago, total);

            System.out.println(facturaData.getNumeroRef() + " " + facturaData.getDatosCliente() + " " + facturaData.getFechaDeVenta().toString() + " "
                    + facturaData.getMedioDePago() + " " + facturaData.getTotalVenta());

            System.out.println("se añadira una factura a la bdFactura");

            bdFactura.registrar(facturaData);


            //ItemFactura(String nroRefFactura,String codRefProd, String descripcion,  String valorUnitario, String cantidad, String subtotal)
            for (int i = 0; i < listaproductosAddedToFactura.size(); i++) {
                ItemFactura itemFactura = new ItemFactura(numeroRef, listaproductosAddedToFactura.get(i).getCodigo(),
                        listaproductosAddedToFactura.get(i).getDescripcion(), listaproductosAddedToFactura.get(i).getPrecioVenta(), "" + 1,
                        listaproductosAddedToFactura.get(i).getPrecioVenta());

                bdItemFactura.registrar(itemFactura);
            }

            limpiarCamposFacturacion();
            numeracionFactura.setText("000" + fijarConsecutivo());
        } else {
            controladoraIni.getMainApp().mostrarVentanaError();
        }
    }


    //limpiar campos de facturacion.
    public void limpiarCamposFacturacion() {
        selectorFecha.getEditor().setText("Seleccionar fecha");
        cbSelectorClientes.getSelectionModel().clearSelection();
        cbSelectorPagoFactura.getSelectionModel().clearSelection();

        if (listaproductosAddedToFactura.size() > 0) {
            tablaProductosFactura.getItems().remove(0, listaproductosAddedToFactura.size());
        }

        nombreCliente.setText("");
        telefonoCliente.setText("");
        ccCliente.setText("");
        emailCliente.setText("");
        direccionCliente.setText("");
        fechaFactura = null;
        listaproductosAddedToFactura = null;
        lValorTotal.setText("");
    }


    //permite abrir la ventana de crear cliente
    @FXML
    public void crearCliente() {
        System.out.println("se creara un cliente");
        revisarFecha();
        controladoraIni.setObjFact(this);
        controladoraIni.setIngresoFromFacturacion(true);

        //se desactiva runningprocess
        controladoraIni.setRunningProcess(false);
        controladoraIni.mostrarPanelClientes();
    }


}
