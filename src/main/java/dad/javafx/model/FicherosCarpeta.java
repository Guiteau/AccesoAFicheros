package dad.javafx.model;

import java.io.File;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class FicherosCarpeta {

	private File fichero_carpeta;
	private StringProperty nombre = new SimpleStringProperty();
	private StringProperty tipo = new SimpleStringProperty();

	public FicherosCarpeta(File fichero_carpeta, String nombre, String tipo) {

		this.fichero_carpeta = fichero_carpeta;
		this.nombre.set(nombre);
		this.tipo.set(tipo);
	
	}
	
	public FicherosCarpeta() {
		
		
	}

	public File getFichero_carpeta() {
		return fichero_carpeta;
	}

	public void setFichero_carpeta(File fichero_carpeta) {
		this.fichero_carpeta = fichero_carpeta;
	}

	public final StringProperty nombreProperty() {
		return this.nombre;
	}

	public final String getNombre() {
		return this.nombreProperty().get();
	}

	public final void setNombre(final String nombre) {
		this.nombreProperty().set(nombre);
	}

	public final StringProperty tipoProperty() {
		return this.tipo;
	}

	public final String getTipo() {
		return this.tipoProperty().get();
	}

	public final void setTipo(final String tipo) {
		this.tipoProperty().set(tipo);
	}

}
