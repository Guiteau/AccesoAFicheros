package dad.javafx.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import org.apache.commons.io.FileUtils;

import dad.javafx.model.FicherosCarpeta;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class AccesoFicherosController implements Initializable {

	// MODEL

	private ObservableList<FicherosCarpeta> observableList_ficherosCarpeta = FXCollections.observableArrayList();

	// VIEW

	@FXML
	private GridPane accesoAFicherosView;

	@FXML
	private Button button_crear, button_eliminar, button_mover, button_verContenidoFichero, button_modificarFichero,
			button_verFicherosCarpetas;

	@FXML
	private CheckBox checkBox_esCarpeta, checkBox_esFichero;

	@FXML
	private TextField textField_crearEliminarMover, textField_rutaActual;

	@FXML
	private TableView<FicherosCarpeta> tableView_listado;

	@FXML
	private TableColumn<FicherosCarpeta, String> tableColumn_nombre;

	@FXML
	private TableColumn<FicherosCarpeta, String> tableColumn_tipo;

	@FXML
	private TextArea textArea_verContenidoFichero;

	public GridPane getAccesoAFicherosView() {
		return accesoAFicherosView;
	}

	public AccesoFicherosController() throws IOException {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccesoAFicherosView.fxml"));
		loader.setController(this);
		loader.load();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// EVENTOS

		button_crear.setOnAction(e -> onCrearAction(e));
		button_eliminar.setOnAction(e -> onEliminarAction(e));
		button_mover.setOnAction(e -> onMoverAction(e));

		button_verFicherosCarpetas.setOnAction(e -> onVerFicherosCarpetasAction(e));

		button_verContenidoFichero.setOnAction(e -> onVerContenidoFicheroAction(e));

		button_modificarFichero.setOnAction(e -> onModificarFicheroAction(e));

		// LISTENERS

		checkBox_esCarpeta.selectedProperty().addListener((o, ov, nv) -> onCheckBox_esCarpetaSelected(o, ov, nv));
		checkBox_esFichero.selectedProperty().addListener((o, ov, nv) -> onCheckBox_esFichero(o, ov, nv));

		// COLUMNAS

		tableView_listado.setEditable(true);

		tableColumn_nombre.setCellValueFactory(v -> v.getValue().nombreProperty());
		tableColumn_nombre.setCellFactory(TextFieldTableCell.forTableColumn());

		tableColumn_tipo.setCellValueFactory(v -> v.getValue().tipoProperty());
		
	}

	private void onCheckBox_esCarpetaSelected(ObservableValue<? extends Boolean> o, Boolean ov, Boolean nv) {

		if (nv == true && checkBox_esFichero.isSelected()) {

			checkBox_esFichero.setSelected(false);

		}

	}

	private void onCheckBox_esFichero(ObservableValue<? extends Boolean> o, Boolean ov, Boolean nv) {

		if (nv == true && checkBox_esFichero.isSelected()) {

			checkBox_esCarpeta.setSelected(false);

		}

	}

	private void onCrearAction(ActionEvent e) {

		boolean ficheroCreado, carpetaCreada;

		Path path_fichero_carpeta, path_fichero_carpeta_destino;

		path_fichero_carpeta = Paths.get(textField_rutaActual.getText());

		path_fichero_carpeta_destino = Paths.get(textField_crearEliminarMover.getText());

		File fichero_carpeta = new File(path_fichero_carpeta.toString(), path_fichero_carpeta_destino.toString());

		if (!textField_rutaActual.getText().isEmpty() && fichero_carpeta.exists()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("El fichero/carpeta ya existe");

			alert.showAndWait();

		} else {

			if (checkBox_esCarpeta.isSelected()) {

				carpetaCreada = fichero_carpeta.mkdir();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Información");
				alert.setHeaderText("");

				if (carpetaCreada) {

					alert.setContentText("Carpeta creada con éxito");

				} else {

					alert.setContentText("Algo ha fallado al crear la carpeta, inténtalo de nuevo");

				}

				alert.showAndWait();

			} else if (checkBox_esFichero.isSelected()) {

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Información");
				alert.setHeaderText("");

				try {

					ficheroCreado = fichero_carpeta.createNewFile();

					if (ficheroCreado) {

						alert.setContentText("Fichero creado con éxito");

					} else {

						alert.setContentText("Algo ha fallado al crear el fichero, inténtalo de nuevo");

					}

					alert.showAndWait();

				} catch (IOException e1) {

					System.out.println("Error evento botón crear <onCrearAction> " + e1.getMessage());

				}

			}

		}

	}

	private void onMoverAction(ActionEvent e) {

		String fichero_carpeta = "";

		Path path_fichero_carpeta_actual, path_fichero_carpeta_destino;

		path_fichero_carpeta_actual = Paths.get(textField_rutaActual.getText());

		path_fichero_carpeta_destino = Paths.get(textField_crearEliminarMover.getText());

		File from = new File(path_fichero_carpeta_actual.toString());
		File to = new File(path_fichero_carpeta_destino.toString());

		fichero_carpeta = (from.isDirectory() ? "carpeta" : "fichero");

		if (this.checkboxVacios()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Debes marcar si es fichero o carpeta");
			alert.showAndWait();

		} else if (textField_crearEliminarMover.getText().isEmpty() && !textField_rutaActual.getText().isEmpty()) {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("");
			alert.setContentText("Ha habido un error al mover el/la " + fichero_carpeta + ", comprueba los campos");
			alert.showAndWait();

		} else {

			if (checkBox_esCarpeta.isSelected()) {

				if (from.isFile()) {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("");
					alert.setContentText("Ha habido un error al mover el " + fichero_carpeta + ", has elegido carpeta");
					alert.showAndWait();

				} else if (from.isDirectory()) {

					boolean exito = from.renameTo(new File(to, from.getName()));

					if (exito) {
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Información");
						alert.setHeaderText("");
						alert.setContentText("Carpeta movido con éxito");
						alert.showAndWait();

					} else {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("");
						alert.setContentText("Ha habido un error al mover la " + fichero_carpeta + ", inténtalo de nuevo.");
						alert.showAndWait();

					}

				}

			} else if (checkBox_esFichero.isSelected()) {

				if (from.isDirectory()) {

					Alert alert = new Alert(AlertType.ERROR);
					alert.setTitle("Error");
					alert.setHeaderText("");
					alert.setContentText("Ha habido un error al mover la " + fichero_carpeta + ", has elegido fichero");
					alert.showAndWait();

				} else if (from.isFile()) {

					boolean exito = from.renameTo(new File(to, from.getName()));

					if (exito) {
						
						Alert alert = new Alert(AlertType.INFORMATION);
						alert.setTitle("Información");
						alert.setHeaderText("");
						alert.setContentText("Fichero movido con éxito");
						alert.showAndWait();

					} else {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("");
						alert.setContentText("Ha habido un error al mover el " + fichero_carpeta + ", inténtalo de nuevo.");
						alert.showAndWait();

					}

				}

			}

		}

	}

	private void onEliminarAction(ActionEvent e) {

		Path selectedToDelete = Paths
				.get(textField_rutaActual.getText() + "\\\\" + textField_crearEliminarMover.getText());

		File fichero_carpeta = new File(selectedToDelete.toString());

		if (checkBox_esCarpeta.isSelected()) {

			if (fichero_carpeta.isFile()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("");
				alert.setContentText("El elemento seleccionado es un fichero y has marcado carpeta");
				alert.showAndWait();

			} else if (fichero_carpeta.isDirectory()) {

				try {

					FileUtils.deleteDirectory(fichero_carpeta);

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Información");
					alert.setHeaderText("");
					alert.setContentText("Carpeta eliminada con éxito");
					alert.showAndWait();

				} catch (IOException e1) {

					if (textField_rutaActual.getText().isEmpty() || textField_crearEliminarMover.getText().isEmpty()) {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("");
						alert.setContentText("Algo ha fallado, hay algún campo vacío");
						alert.showAndWait();

						System.out.println("Error en <onEliminarAction> " + e1.getMessage());

					}

				}

			}

		} else if (checkBox_esFichero.isSelected()) {

			if (fichero_carpeta.isDirectory()) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("");
				alert.setContentText("El elemento seleccionado es una carpeta y has marcado fichero");
				alert.showAndWait();

			} else if (fichero_carpeta.isFile()) {

				if (fichero_carpeta.delete()) {

					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Información");
					alert.setHeaderText("");
					alert.setContentText("Fichero eliminado con éxito");
					alert.showAndWait();

				} else {

					if (!textField_rutaActual.getText().isEmpty()) {

						Alert alert = new Alert(AlertType.ERROR);
						alert.setTitle("Error");
						alert.setHeaderText("");
						alert.setContentText("Algo ha fallado, inténtalo de nuevo");
						alert.showAndWait();

					}

				}

			}

		}

	}

	private void onVerFicherosCarpetasAction(ActionEvent e) {

		observableList_ficherosCarpeta.clear();

		Path path_fichero_carpeta_actual;

		if (!textField_rutaActual.getText().isEmpty()) {

			path_fichero_carpeta_actual = Paths.get(textField_rutaActual.getText());

			File fichero_carpeta = new File(path_fichero_carpeta_actual.toString());

			File[] listaFicherosCarpetas = fichero_carpeta.listFiles();

			if (listaFicherosCarpetas != null) {

				for (int i = 0; i < listaFicherosCarpetas.length; i++) {

					if (listaFicherosCarpetas[i].isFile()) {

						observableList_ficherosCarpeta.add(new FicherosCarpeta(listaFicherosCarpetas[i],
								listaFicherosCarpetas[i].getName(), "Fichero"));

					} else if (listaFicherosCarpetas[i].isDirectory()) {

						observableList_ficherosCarpeta.add(new FicherosCarpeta(listaFicherosCarpetas[i],
								listaFicherosCarpetas[i].getName(), "Carpeta"));

					}

				}

			} else {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("");
				alert.setContentText("Algo ha fallado, comprueba que los datos introducidos sean correctos");
				alert.showAndWait();

			}

			tableView_listado.setItems(observableList_ficherosCarpeta);

		}

	}

	private void onVerContenidoFicheroAction(ActionEvent e) {

		File file_elegido = tableView_listado.getSelectionModel().getSelectedItem().getFichero_carpeta();

		String contenido = "";

		if (file_elegido != null) {

			try (BufferedReader br = new BufferedReader(new FileReader(file_elegido))) {

				String line;

				while ((line = br.readLine()) != null) {

					contenido += line;
					contenido += "\n";

				}

				br.close();

			} catch (IOException e1) {

				System.out.println("Error en <onVerContenidoFicheroAction> " + e1.getMessage());

			}

			textArea_verContenidoFichero.setText(contenido);
			textArea_verContenidoFichero.setWrapText(true);

		}

	}

	private void onModificarFicheroAction(ActionEvent e) {

		File file_elegido = tableView_listado.getSelectionModel().getSelectedItem().getFichero_carpeta();

		if (file_elegido != null) {

			try {

				FileWriter fw = new FileWriter(file_elegido);

				fw.write(textArea_verContenidoFichero.getText());

				fw.close();

			} catch (IOException e1) {

				System.out.println("Error en <onModificarFicheroAction> " + e1.getMessage());

			}

		}

	}

	public boolean checkboxVacios() {

		return (!checkBox_esCarpeta.isSelected() && !checkBox_esFichero.isSelected());

	}

}
