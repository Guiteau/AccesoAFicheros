package dad.javafx.accesoFicheros;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javafx.controller.AccesoAleatorioController;
import dad.javafx.controller.AccesoFicherosController;
import dad.javafx.controller.AccesoXMLController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class MainController implements Initializable {
	
	// CONTROLLERS
	
	private AccesoFicherosController accesoFicherosController = new AccesoFicherosController();
	private AccesoAleatorioController accesoAleatorioController = new AccesoAleatorioController();
	private AccesoXMLController accesoXMLController = new AccesoXMLController();
	
	// VIEW
	
    @FXML
    private TabPane mainView;

    @FXML
    private Tab tab_accesoAficheros;

    @FXML
    private Tab tab_accesoAleatorio;

    @FXML
    private Tab tab_accesoXML;

	public TabPane getMainView() {
		return mainView;
	}

	public MainController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainView.fxml"));
		loader.setController(this);
		loader.load();
	}
    
	@Override
	public void initialize(URL location, ResourceBundle resources){

		tab_accesoAficheros.setContent(accesoFicherosController.getAccesoAFicherosView());
		tab_accesoAleatorio.setContent(accesoAleatorioController.getAccesoAleatorioView());
		tab_accesoXML.setContent(accesoXMLController.getAccesoXMLView());
		
	}

}
