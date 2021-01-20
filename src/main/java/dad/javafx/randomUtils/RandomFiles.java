package dad.javafx.randomUtils;

import java.io.IOException;
import java.io.RandomAccessFile;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class RandomFiles {

	private final static char COMA = ',';

	public void insertarDatos(String[] equipo) throws IOException {

		int codEquipo;

		RandomAccessFile ficheroRandom = new RandomAccessFile("equipos.dat", "rw");

		if (ficheroRandom.length() == 0) {

			codEquipo = 1;

		} else { // 4 + 2 + 80 + 2 + 10 + 2 + 120 + 2 + 4 + 2 + 1 + 2 = 231 bytes

			ficheroRandom.seek(ficheroRandom.length() - (4 + 2 + 80 + 2 + 10 + 2 + 120 + 2 + 4 + 2 + 1 + 2));

			codEquipo = ficheroRandom.readInt() + 1;

			ficheroRandom.seek(ficheroRandom.length());
		}

		ficheroRandom.seek(ficheroRandom.length());

		ficheroRandom.writeInt(codEquipo);
		ficheroRandom.writeChar(COMA);
		ficheroRandom.writeChars(equipo[0]); // nomEquipo
		ficheroRandom.writeChar(COMA);
		ficheroRandom.writeChars(equipo[1]); // codLiga
		ficheroRandom.writeChar(COMA);
		ficheroRandom.writeChars(equipo[2]); // localidad
		ficheroRandom.writeChar(COMA);
		ficheroRandom.writeInt(Integer.parseInt(equipo[3])); // copas
		ficheroRandom.writeChar(COMA);
		ficheroRandom.writeBoolean(Boolean.parseBoolean(equipo[4])); // internacional
		ficheroRandom.writeChar(COMA);

		ficheroRandom.close();

	}

	public void modificarFicheroCopasGanadas(int codEquipo, int copasGanadas) throws IOException {

		try (RandomAccessFile ficheroRandom = new RandomAccessFile("equipos.dat", "rw")) {

			if (ficheroRandom.length() == 0) {

				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Error");
				alert.setHeaderText("Fichero vacío");
				alert.setContentText("El fichero no contiene datos");

				alert.showAndWait();

			} else {

				codEquipo = (codEquipo - 1) * 231;
				ficheroRandom.seek(codEquipo + (4 + 2 + 80 + 2 + 10 + 2 + 120 + 2));

				ficheroRandom.writeInt(copasGanadas);

				ficheroRandom.close();
			}
		}

	}

	public String verTodo() throws IOException {

		String contenido = "";

		try (RandomAccessFile ficheroRandom = new RandomAccessFile("equipos.dat", "rw")) {
			ficheroRandom.seek(0);

			while (ficheroRandom.getFilePointer() < ficheroRandom.length()) {

				contenido += Integer.toString(ficheroRandom.readInt()); // añadimos primero el código de equipo

				for (int i = 0; i <= 108; i++) { // 108 = nº de caracteres desde la coma después de codEquipo hasta la
													// coma después de localidad

					contenido += Character.toString(ficheroRandom.readChar());

				}

				contenido += Integer.toString(ficheroRandom.readInt()); // copas ganadas
				contenido += Character.toString(ficheroRandom.readChar());
				contenido += Boolean.toString(ficheroRandom.readBoolean());
				contenido += Character.toString(ficheroRandom.readChar());
				contenido += "\n";

			}
		}

		return contenido;
	}

	public String verRegistro(int codEquipo) throws IOException {

		String registro = "";
		int puntero = 0;

		try (RandomAccessFile ficheroRandom = new RandomAccessFile("equipos.dat", "rw")) {

			ficheroRandom.seek(0);

			while (puntero != codEquipo) {
				
				registro = "";

				puntero = ficheroRandom.readInt();

				registro += Integer.toString(puntero);

				for (int i = 0; i <= 108; i++) { // 108 = nº de caracteres desde la coma después de codEquipo hasta la
													// coma después de localidad

					registro += Character.toString(ficheroRandom.readChar());

				}

				registro += Integer.toString(ficheroRandom.readInt()); // copas ganadas
				registro += Character.toString(ficheroRandom.readChar());
				registro += Boolean.toString(ficheroRandom.readBoolean());
				registro += Character.toString(ficheroRandom.readChar());
				registro += "\n";

			}

		}

		return registro;
	}
	
	public String verUltimoCodEquipo() throws IOException {
		
		String codEquipo = "";
		
		try (RandomAccessFile ficheroRandom = new RandomAccessFile("equipos.dat", "rw")) {
			
			if(ficheroRandom.length() == 0) {
				
				codEquipo = "No hay registros";
				
			} else {

				ficheroRandom.seek(ficheroRandom.length() - (4 + 2 + 80 + 2 + 10 + 2 + 120 + 2 + 4 + 2 + 1 + 2));
				
				codEquipo = String.valueOf(ficheroRandom.readInt());
				
			}
		}
		
		return codEquipo;
		
	}

}
