package maker.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import maker.model.Proveedor;
import maker.model.ProveedorBD;
import maker.model.Usuario;

public class ControladoraProveedorAdd {

    @FXML
    private TextField tfCodigo;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfNIT;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfDireccion;

    //************************ se crea obj de tipo controladora inicio
    private ControladoraInicio controladoraIni;

    public ControladoraInicio getControladoraIni() {
        return controladoraIni;
    }

    public void setControladoraIni(ControladoraInicio controladoraIni) {
        this.controladoraIni = controladoraIni;
    }
    //************************

    private Proveedor proveedorToEditar;


    private Stage stage;

    public void setStage(Stage stage){
        this.stage = stage;
    }

    private ProveedorBD bd;
    private boolean isForEditar;

    @FXML
    public void initialize(){
        isForEditar = false;
        bd = new ProveedorBD();

    }

    //cierra la ventana en el momento de dar clic en aceptar.
    @FXML
    public void regresar(){
        stage.close();
    }

    @FXML
    public void aceptar(){
        if(validarCamposVacios()){

            String nombre = tfNombre.getText();
            String codigo = tfCodigo.getText();
            String nit = tfNIT.getText();
            String telefono = tfTelefono.getText();
            String direccion = tfDireccion.getText();
            String email = tfEmail.getText();

            Proveedor proveedor = new Proveedor(codigo,nit,nombre,direccion,telefono,email);

            System.out.println("se añadira un proveedor a la bd");
            System.out.println(isForEditar + "idforeditar");

            if(!isForEditar){
                    bd.registrar(proveedor);
                System.out.println(isForEditar + "idforeditar");
            }
            else{
                isForEditar = false;
                bd.actualizar(proveedor);
            }
            controladoraIni.mostrarPanelProveedores();

            stage.close();
        }

    }


    public void mostrarDatos(Proveedor proveedorToEditar){
        this.proveedorToEditar = proveedorToEditar;
        tfNombre.setText(proveedorToEditar.getNombre());
        tfCodigo.setText(proveedorToEditar.getCodigo());
        tfNIT.setText(proveedorToEditar.getNit());
        tfTelefono.setText(proveedorToEditar.getTelefono());
        tfEmail.setText(proveedorToEditar.getEmail());
        tfDireccion.setText(proveedorToEditar.getDireccion());
        isForEditar = true;
    }


    public boolean validarCamposVacios(){
        if(tfNombre.getText().isEmpty() || tfCodigo.getText().isEmpty() || tfNIT.getText().isEmpty()  ||
                 tfDireccion.getText().isEmpty() || tfTelefono.getText().isEmpty() || tfEmail.getText().isEmpty() ){
            System.out.println("error de datos, falta completar");
            return false;
        }
        return true;
    }

}
