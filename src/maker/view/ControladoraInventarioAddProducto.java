package maker.view;

import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import maker.model.Producto;
import maker.model.ProductoBD;
import maker.model.Proveedor;

public class ControladoraInventarioAddProducto {

    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfCantidad;
    @FXML
    private TextField tfArtista;
    @FXML
    private TextField tfAlbum;
    @FXML
    private TextField tfCoste;
    @FXML
    private TextField tfPrecioVenta;
    @FXML
    private TextField tfProveedor;
    @FXML
    private TextField tfTipo;
    @FXML
    private TextField tfGenero;


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

    private Producto productoToEditar;

    private ProductoBD bd;

    //cuando se envia un producto a la ventana de editar producto
    private boolean isForEditar;

    @FXML
    public void initialize(){

        bd = new ProductoBD();
        isForEditar = false;
    }

    //cierra la ventana en el momento de dar clic en aceptar.
    @FXML
    public void regresar(){
        stage.close();
    }

    @FXML
    public void aceptar(){
        if(validarCamposVacios()){

            String codigo = tfCodigo.getText();
            String nombreProveedor = tfProveedor.getText();
            String tipo = tfTipo.getText();
            String artista = tfArtista.getText();
            String album = tfAlbum.getText();
            String genero = tfGenero.getText();
            String coste = tfCoste.getText();
            String precioVenta = tfPrecioVenta.getText();
            String cantidad = tfCantidad.getText();

            Producto producto = new Producto(codigo,nombreProveedor,tipo,artista,album,genero,coste,precioVenta,cantidad);

            System.out.println("se añadira un producto a la bd");

            System.out.println("isforeditar"+isForEditar);
            if(!isForEditar) {
                if(!bd.registrar(producto)){
                    controladoraIni.getMainApp().mostrarVentanaError();
                }
            }
            else{
                isForEditar = false;
                if(!bd.actualizar(producto)){
                    controladoraIni.getMainApp().mostrarVentanaError();
                }
            }
            controladoraIni.mostrarPanelInventario();
            stage.close();
        }

    }


    public void mostrarDatos(Producto productoToEditar){
        this.productoToEditar = productoToEditar;
        tfCodigo.setText(productoToEditar.getCodigo());
        tfCantidad.setText(productoToEditar.getCantidad());
        tfArtista.setText(productoToEditar.getArtista());
        tfAlbum.setText(productoToEditar.getAlbum());
        tfCoste.setText(productoToEditar.getCoste());
        tfPrecioVenta.setText(productoToEditar.getPrecioVenta());
        tfProveedor.setText(productoToEditar.getUnProveedor());
        tfTipo.setText(productoToEditar.getTipo());
        tfGenero.setText(productoToEditar.getGenero());
        isForEditar = true;
    }


    public boolean validarCamposVacios(){
        if(tfCodigo.getText().isEmpty() || tfCantidad.getText().isEmpty() || tfArtista.getText().isEmpty()  ||
                tfAlbum.getText().isEmpty() || tfCoste.getText().isEmpty() || tfPrecioVenta.getText().isEmpty() ||
                tfGenero.getText().isEmpty() || tfTipo.getText().isEmpty() ||
                tfProveedor.getText().isEmpty()){
            System.out.println("error de datos, falta completar");
            return false;
        }
        return true;
    }
}
