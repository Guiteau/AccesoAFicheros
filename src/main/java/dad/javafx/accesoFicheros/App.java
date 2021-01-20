package dad.javafx.accesoFicheros;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class App extends Application {
	
	MainController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {

		controller = new MainController();	
		
		Scene escena = new Scene(controller.getMainView());
		
		primaryStage.getIcons().add(new Image("icons/folderIcon32x32.png"));
		primaryStage.setScene(escena);
		primaryStage.setTitle("Acceso a ficheros\t");
		primaryStage.show();
		
	}

	public static void main(String[] args) {

		launch(args);
		
	}

}
