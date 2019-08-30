package maker.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import maker.MainApp;
import maker.model.Usuario;
import maker.model.UsuarioBD;

import java.io.IOException;

public class ControladoraLogin {

    @FXML
    private TextField userName;
    @FXML
    private PasswordField password;

    //se crea un objeto de tipo mainApp
    private MainApp mainApp;

    private UsuarioBD bd;


    //constructor
    public ControladoraLogin() {
    }

    //inicializador
    public void initialize() {
        bd = new UsuarioBD();
    }

    //cargamos una variable para login layout
    private AnchorPane loginLayout;


    //se toma el objeto de tipo main para interactuar ventanas
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }


    //cuando se presiona el boton iniciar sesion
    @FXML
    public void iniciarSesion() {
        Usuario usuarioEncontrado = validacionUsuario();
        if (usuarioEncontrado != null) {
            mostrarMenuInicio(usuarioEncontrado);
        } else {
            mainApp.mostrarVentanaError();
        }
    }


    //metodo para validar al usuario
    public Usuario validacionUsuario() {
        if (!userName.getText().isEmpty() && !password.getText().isEmpty()) {

            //aca es donde valida la parte del usuario en la bd
            String usuario = userName.getText();
            int contrasena = Integer.parseInt(password.getText());

            Usuario usuarioEncontrado = bd.consultar(usuario);

            if (usuarioEncontrado != null) {
                Password password = new ExtendedPassword(Integer.parseInt(usuarioEncontrado.getContrasenna()));

                if (contrasena == password.getEncryptedPassword()) {
                    return usuarioEncontrado;
                }
            }
        }
        return null;
    }


    //mis pruebas para ajustar organizacion
    //se muestra la ventana de inicio luego de iniciar sesion
    public void mostrarMenuInicio(Usuario usuarioEncontrado) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(ControladoraLogin.class.getResource("inicio.fxml"));
            loginLayout = (AnchorPane) loader.load();

            //se muestra la escena contenida en el loginLayout
            Scene scene = new Scene(loginLayout);
            mainApp.getPrimaryStage().setScene(scene);


            //da al controlador acceso a la mainapp
            ControladoraInicio controladora = loader.getController();
            controladora.setMainApp(mainApp);
            controladora.setUsuarioLogueado(usuarioEncontrado);
            controladora.getlUsuarioLogueado().setText(usuarioEncontrado.getNombre() + " " + usuarioEncontrado.getApellido());

            //nuevo para split
            controladora.setInicioStage(mainApp.getPrimaryStage());

            mainApp.getPrimaryStage().show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
