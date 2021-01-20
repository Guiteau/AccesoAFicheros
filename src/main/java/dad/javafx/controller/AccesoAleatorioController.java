package dad.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import dad.javafx.randomUtils.RandomFiles;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class AccesoAleatorioController implements Initializable {

	// UTILS

	private RandomFiles randomFiles_metodos = new RandomFiles();

	// VIEW

	@FXML
	private BorderPane accesoAleatorioView;

	@FXML
	private TextArea textArea_accesoAleatorio;

	@FXML
	private TextField textField_codEquipo, textField_nomEquipo, textField_codLiga, textField_localidad,
			textField_copasGanadas, textField_contadorCharsNombre, textField_contadorCharsLocalidad;

	@FXML
	private CheckBox checkBox_internacional;

	@FXML
	private Button button_insertar, button_modificar, button_visualizarRegistro, button_visualizarTodo,
			button_insercionAutomatica, button_ultimoCodEquipo;

	public BorderPane getAccesoAleatorioView() {
		return accesoAleatorioView;
	}

	public AccesoAleatorioController() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccesoAleatorioView.fxml"));
		loader.setController(this);
		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// textField_codEquipo.setDisable(true);
		textField_codEquipo.setAlignment(Pos.CENTER);
		textField_contadorCharsNombre.setAlignment(Pos.CENTER);
		textField_contadorCharsLocalidad.setAlignment(Pos.CENTER);

		textField_codEquipo.isPressed();

		// EVENTOS

		button_ultimoCodEquipo.setOnAction(e -> {
			try {
				onUltimoCodEquipoAction (e);
			} catch (IOException e3) {
				e3.printStackTrace();
			}
		});
		button_insercionAutomatica.setOnAction(e -> {
			try {
				onInsercionAutomaticaAction(e);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		button_insertar.setOnAction(e -> {
			try {
				onInsertarAction(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		button_modificar.setOnAction(e -> onModificarAction(e));
		button_visualizarRegistro.setOnAction(e -> {
			try {
				onVisualizarRegistroAction(e);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
		});
		button_visualizarTodo.setOnAction(e -> {
			try {
				onVisualizarTodoAction(e);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		});
		
		// BINDEOS

		textField_contadorCharsNombre.textProperty()
				.bind(Bindings.length(textField_nomEquipo.textProperty()).asString());
		textField_contadorCharsLocalidad.textProperty()
				.bind(Bindings.length(textField_localidad.textProperty()).asString());

	}

	private void onUltimoCodEquipoAction(ActionEvent e) throws IOException {
		
		textField_codEquipo.setText(randomFiles_metodos.verUltimoCodEquipo());
		
	}

	private void onInsercionAutomaticaAction(ActionEvent e) throws IOException {

		String equipo1[] = { "FC Barcelona                            ", "PDN  ",
				"Barcelona                                                   ", "60", "true" };
		randomFiles_metodos.insertarDatos(equipo1);

		String equipo2[] = { "Club Atlético Osasuna                   ", "PDN  ",
				"Pamplona                                                    ", "0", "false" };
		randomFiles_metodos.insertarDatos(equipo2);

		String equipo3[] = { "Valencia Club de Fútbol                 ", "PDN  ",
				"Valencia                                                    ", "5", "true" };
		randomFiles_metodos.insertarDatos(equipo3);

		String equipo4[] = { "Real Sporting de Gijón                  ", "SDN  ",
				"Gijón                                                       ", "1", "false" };
		randomFiles_metodos.insertarDatos(equipo4);

	}

	private void onInsertarAction(ActionEvent e) throws IOException {

		String nomEquipo = "", codLiga = "", localidad = "", copasGanadas = "" , internacional = "";

		String equipo[] = new String[5];

		// nomEquipo == 40
		// codLiga == 5
		// localidad == 60
		// copas ganadas

		nomEquipo = textField_nomEquipo.getText();
		codLiga = textField_codLiga.getText();
		localidad = textField_localidad.getText();
		copasGanadas = textField_copasGanadas.getText();
		internacional = (checkBox_internacional.isSelected())?"true":"false";

		while (nomEquipo.length() < 40) {

			nomEquipo += ' ';

		}

		while (codLiga.length() < 5) {

			codLiga += ' ';

		}

		while (localidad.length() < 60) {

			localidad += ' ';

		}
		
		equipo [0] = nomEquipo;
		equipo [1] = codLiga;
		equipo [2] = localidad;
		equipo [3] = copasGanadas;
		equipo [4] = internacional;
		
		randomFiles_metodos.insertarDatos(equipo);

	}

	private void onModificarAction(ActionEvent e) {
		
		int codEquipo, copasGanadas;
		
		codEquipo = Integer.parseInt(textField_codEquipo.getText());
		copasGanadas = Integer.parseInt(textField_copasGanadas.getText());
		
		try {
			randomFiles_metodos.modificarFicheroCopasGanadas(codEquipo, copasGanadas);
		} catch (IOException e1) {
			e1.printStackTrace();
		}

	}

	private void onVisualizarRegistroAction(ActionEvent e) throws IOException {

		int codEquipo = Integer.parseInt(textField_codEquipo.getText());
		
		textArea_accesoAleatorio.clear();
				
		textArea_accesoAleatorio.setText(randomFiles_metodos.verRegistro(codEquipo));
		
	}

	private void onVisualizarTodoAction(ActionEvent e) throws IOException {

		textArea_accesoAleatorio.clear();
		
		textArea_accesoAleatorio.setText(randomFiles_metodos.verTodo());

	}

}
