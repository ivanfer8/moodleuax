package conv.android.moodle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fac.android.moodle.UserSingleton;

public class CheckSrcreen {

	private UserSingleton usuSelecc;
	private String error = "";

	public CheckSrcreen() {
		// Constructor
	}

	/*
	 * Comprobamos que los campos introducios por el usuario son correctos
	 */
	public String checkCreateUser() {
		this.usuSelecc = UserSingleton.getInstance();
		// comprobar datos vacios
		if (checkEmptyCreate() && checkCorreo(usuSelecc.getEmail()) && comprobarLogitudes()) {
			return this.error;
		} else {
			return this.error;
		}

	}

	/*
	 * Comprobamos que los campos para editar usuarios son correctos
	 */
	public String checkEditUser() {
		this.usuSelecc = UserSingleton.getInstance();
		// comprobar datos vacios o incorrectos
		if (checkEmptyEdit() && checkCorreo(usuSelecc.getEmail()) && comprobarLogitudesEdit()) {
			return this.error;
		} else {
			return this.error;
		}
	}

	private boolean checkEmptyEdit() {
		// los campos para comprobar que estan vacios son el correo, el nombre y
		// los apellidos
		if (usuSelecc.getName().length() <= 0) {
			this.error = "Rellene el nombre de usuario";
			return false;
		}
		if (usuSelecc.getEmail().length() <= 0) {
			this.error = "Rellene el correo electrónico del usuario";
			return false;
		}
		if (usuSelecc.getFirstName().length() <= 0) {
			this.error = "Rellene el primer apellido";
			return false;
		}
		if (usuSelecc.getLastName().length() <= 0) {
			this.error = "Rellene el segundo apellido";
			return false;
		} else {
			return true;
		}
	}

	private boolean comprobarLogitudes() {
		if (!checkLong(usuSelecc.getName(), 5)) {
			this.error = "Longitud del nombre insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getFirstName(), 5)) {
			this.error = "Longitud del primer apellido insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getLastName(), 5)) {
			this.error = "Longitud del segundo apellido insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getEmail(), 5)) {
			this.error = "Longitud del correo electrónico insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getPass(), 5)) {
			this.error = "Longitud del password insuficiente";
			return false;
		} else {
			return true;
		}
	}

	private boolean comprobarLogitudesEdit() {
		if (!checkLong(usuSelecc.getName(), 5)) {
			this.error = "Longitud del nombre insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getFirstName(), 5)) {
			this.error = "Longitud del primer apellido insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getLastName(), 5)) {
			this.error = "Longitud del segundo apellido insuficiente";
			return false;
		}
		if (!checkLong(usuSelecc.getEmail(), 5)) {
			this.error = "Longitud del correo electrónico insuficiente";
			return false;
		} else {
			return true;
		}
	}

	private boolean checkLong(String dato, int longitud) {
		if (dato.length() < longitud) {
			return false;
		} else {
			return true;
		}
	}

	private boolean checkEmptyCreate() {
		// Comprobamos que los campos nombre, apellidos, correo y passwor
		// no esten vacios
		try {
			if (usuSelecc.getName().length() <= 0) {
				this.error = "Rellene el nombre de usuario";
				return false;
			}
			if (usuSelecc.getFirstName().length() <= 0) {
				this.error = "Rellene el primer apellido";
				return false;
			}
			if (usuSelecc.getLastName().length() <= 0) {
				this.error = "Rellene el segundo apellido";
				return false;
			}
			if (usuSelecc.getEmail().length() <= 0) {
				this.error = "Rellene el correo electrónico";
				return false;
			}
			if (usuSelecc.getPass().length() <= 0) {
				this.error = "Rellene el password";
				return false;
			} else {
				return true;
			}
		} catch (Exception e) {
			// Se ha producido algun error, por lo tanto algo está mal
			return false;
		}
	}

	private boolean checkCorreo(String input) {
		try {
			Pattern pattern;
			Matcher matcher;
			pattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
			matcher = pattern.matcher(input);
			if (!matcher.matches()) {
				this.error = "Formato de correo electrónico incorrecto";
				return false;
			} else {
				return true;
			}
		} catch (PatternSyntaxException e) {
			// Se ha producido un error
			return false;
		} catch (IllegalStateException e) {
			// Se ha producido un error
			e.getMessage();
			System.out.println(e.getMessage());
			return false;
		}
	}
}
