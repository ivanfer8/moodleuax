package conv.android.moodle;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import fac.android.moodle.UserSingleton;

public class CheckSrcreen {

	private UserSingleton usuSelecc;

	public CheckSrcreen() {
		// Constructor
	}

	/*
	 * Comprobamos que los campos introducios por el usuario son correctos
	 */
	public boolean checkCreateUser() {
		this.usuSelecc = UserSingleton.getInstance();
		// comprobar datos vacios
		if (checkEmptyCreate() && checkCorreo(usuSelecc.getEmail()) && comprobarLogitudes()) {
			return true;
		} else {
			return false;
		}

	}

	/*
	 * Comprobamos que los campos para editar usuarios son correctos
	 */
	public boolean checkEditUser() {
		this.usuSelecc = UserSingleton.getInstance();
		// comprobar datos vacios o incorrectos
		if (checkEmptyEdit() && checkCorreo(usuSelecc.getEmail())) {
			return true;
		} else {
			return false;
		}
	}

	private boolean checkEmptyEdit() {
		// los campos para comprobar que estan vacios son el correo, el nombre y
		// los apellidos
		if (usuSelecc.getName().length() <= 0 && usuSelecc.getEmail().length() <= 0 && usuSelecc.getFirstName().length() <= 0 && usuSelecc.getLastName().length() <= 0) {
			return false;
		} else {
			return true;
		}
	}

	private boolean comprobarLogitudes() {
		if (checkLong(usuSelecc.getName(), 5) && checkLong(usuSelecc.getFirstName(), 5) && checkLong(usuSelecc.getLastName(), 5) && checkLong(usuSelecc.getEmail(), 5) && checkLong(usuSelecc.getPass(), 5))
			return true;
		else
			return false;
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
			if (usuSelecc.getName().length() <= 0 || usuSelecc.getFirstName().length() <= 0 || usuSelecc.getLastName().length() <= 0 || usuSelecc.getEmail().length() <= 0 || usuSelecc.getPass().length() <= 0) {
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
			return matcher.matches();
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
