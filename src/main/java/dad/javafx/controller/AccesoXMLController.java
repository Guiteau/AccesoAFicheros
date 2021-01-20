package dad.javafx.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.output.Format;
import org.jdom2.output.XMLOutputter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;

public class AccesoXMLController implements Initializable {

	// VIEW

	@FXML
	private BorderPane accesoXMLView;

	@FXML
	private TextArea textArea_XML;

	@FXML
	private Button button_visualizarFichero, button_modificarCopas, button_eliminarEquipo, button_aniadirContrato,
			button_copiarFichero;

	@FXML
	private TextField textField_nombreFichero, textField_nomEquipo, textField_copasGanadas, textField_nomFutbolista;

	@FXML
	private DatePicker datePicker_fechaInicio, datePicker_fechaFin;

	public BorderPane getAccesoXMLView() {
		return accesoXMLView;
	}

	public AccesoXMLController() throws IOException {

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AccesoXMLView.fxml"));
		loader.setController(this);
		loader.load();

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		// EVENTOS

		button_visualizarFichero.setOnAction(e -> {
			try {
				onVisualizarAction(e);
			} catch (JDOMException | IOException e1) {
				e1.printStackTrace();
			}
		});

		button_modificarCopas.setOnAction(e -> {
			try {
				onModificarCopasAction(e);
			} catch (JDOMException | IOException e1) {
				e1.printStackTrace();
			}
		});

		button_eliminarEquipo.setOnAction(e -> {
			try {
				onEliminarEquipoAction(e);
			} catch (JDOMException | IOException e1) {
				e1.printStackTrace();
			}
		});
		
		button_aniadirContrato.setOnAction(e -> {
			try {
				onAniadirContratoAction(e);
			} catch (JDOMException | IOException e1) {
				e1.printStackTrace();
			}
		});
		
		button_copiarFichero.setOnAction(e -> {
			try {
				onCopiarFicheroAction(e);
			} catch (JDOMException | IOException e1) {
				e1.printStackTrace();
			}
		});

	}

	private void onVisualizarAction(ActionEvent e) throws FileNotFoundException, JDOMException, IOException {

		textArea_XML.clear();

		SAXBuilder saxBuilder = new SAXBuilder();

		Document documentJDOM = saxBuilder.build(new FileInputStream("Equipos.xml"));

		Element element = documentJDOM.getRootElement();

		List<Element> listHijos = element.getChildren();

		for (Element hijo : listHijos) {

			textArea_XML.appendText("\nEquipo: " + hijo.getAttributeValue("nomEquipo"));
			textArea_XML.appendText("\n");

			textArea_XML.appendText("Copas ganadas: " + hijo.getAttributeValue("copasGanadas"));
			textArea_XML.appendText("\n");

			textArea_XML.appendText("Código liga: " + hijo.getChildText("codLiga"));
			textArea_XML.appendText("\n");

			List<Element> hijosHijo = hijo.getChildren();

			textArea_XML.appendText("\nContratos\n");
			textArea_XML.appendText("\n");

			for (Element hijo1 : hijosHijo) {

				String nombreEtiqueta = hijo1.getName();

				if (nombreEtiqueta == "Contratos") {

					List<Element> hijosHijosHijo = hijo1.getChildren();

					for (Element hijo2 : hijosHijosHijo) {

						textArea_XML.appendText("Futbolista: " + hijo2.getValue());
						textArea_XML.appendText("\n");
						textArea_XML.appendText("Fecha inicio: " + hijo2.getAttributeValue("fechaInicio"));
						textArea_XML.appendText("\n");
						textArea_XML.appendText("Fecha fin: " + hijo2.getAttributeValue("fechaFin"));
						textArea_XML.appendText("\n");

					}

				}

			}

			textArea_XML.appendText("\n========================");

			textArea_XML.appendText("\n");
		}

	}

	private void onModificarCopasAction(ActionEvent e) throws FileNotFoundException, JDOMException, IOException {

		SAXBuilder saxBuilder = new SAXBuilder();

		Document documentJDOM = saxBuilder.build(new FileInputStream("Equipos.xml"));

		Element element = documentJDOM.getRootElement();

		List<Element> listHijos = element.getChildren();

		String nomEquipo;

		for (int i = 0; i < listHijos.size(); i++) {

			nomEquipo = listHijos.get(i).getAttributeValue("nomEquipo");

			if (nomEquipo != null) {

				if (nomEquipo.equalsIgnoreCase(textField_nomEquipo.getText())) {

					listHijos.get(i).setAttribute("copasGanadas", textField_copasGanadas.getText());

				}

			}

		}

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

		try (FileOutputStream fo = new FileOutputStream("Equipos.xml")) {

			xmlOutputter.output(documentJDOM, fo);

			button_visualizarFichero.fire();

		}

	}

	private void onEliminarEquipoAction(ActionEvent e) throws FileNotFoundException, JDOMException, IOException {

		SAXBuilder saxBuilder = new SAXBuilder();

		Document documentJDOM = saxBuilder.build(new FileInputStream("Equipos.xml"));

		Element element = documentJDOM.getRootElement();

		Element elementoEliminar = null;

		List<Element> listHijos = element.getChildren();

		String nomEquipo;

		for (int i = 0; i < listHijos.size(); i++) {

			nomEquipo = listHijos.get(i).getAttributeValue("nomEquipo");

			if (nomEquipo != null) {

				if (nomEquipo.equalsIgnoreCase(textField_nomEquipo.getText()))

					elementoEliminar = listHijos.get(i);

			}

		}

		listHijos.remove(elementoEliminar);

		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

		try (FileOutputStream fo = new FileOutputStream("Equipos.xml")) {

			xmlOutputter.output(documentJDOM, fo);

			button_visualizarFichero.fire();

		}

	}

	private void onAniadirContratoAction(ActionEvent e) throws FileNotFoundException, JDOMException, IOException {
		
		LocalDate localDateInicio = datePicker_fechaInicio.getValue();
		LocalDate localDateFin = datePicker_fechaFin.getValue(); 

		
		SAXBuilder saxBuilder = new SAXBuilder();

		Document documentJDOM = saxBuilder.build(new FileInputStream("Equipos.xml"));

		Element element = documentJDOM.getRootElement();
		
		Element nuevoElemento;
		
		Element elementoHijo;
		
		List<Element> listHijos = element.getChildren();

		String nomEquipo;
		
		for (int i = 0; i < listHijos.size(); i++) {
			
			nomEquipo = listHijos.get(i).getAttributeValue("nomEquipo");
			
			if(nomEquipo != null) {
				
				if(nomEquipo.equalsIgnoreCase(textField_nomEquipo.getText())) {
					
					nuevoElemento = new Element("Futbolista");
					nuevoElemento.setAttribute("fechaInicio", localDateInicio.toString());
					nuevoElemento.setAttribute("fechaFin", localDateFin.toString());
					nuevoElemento.setText(textField_nomFutbolista.getText());
					elementoHijo = (Element) listHijos.get(i).getChild("Contratos");
					elementoHijo.addContent(nuevoElemento);
					
				}
				
			}
			
			XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

			try (FileOutputStream fo = new FileOutputStream("Equipos.xml")) {

				xmlOutputter.output(documentJDOM, fo);

				button_visualizarFichero.fire();

			}
			
		}
		
	}
	
	private void onCopiarFicheroAction(ActionEvent e) throws FileNotFoundException, JDOMException, IOException {

		SAXBuilder saxBuilder = new SAXBuilder();

		Document documentJDOM = saxBuilder.build(new FileInputStream("Equipos.xml"));
		
		XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());

		FileOutputStream fos = new FileOutputStream(textField_nombreFichero.getText() +".xml");
		
		xmlOutputter.output(documentJDOM, fos);
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Información");
		alert.setHeaderText(null);
		alert.setContentText("Fichero copiado con éxito");

		alert.showAndWait();
		
	}
}
