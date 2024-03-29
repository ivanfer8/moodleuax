package conv.android.moodle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.widget.EditText;
import fac.android.moodle.User;
import fac.android.moodle.UserSingleton;

public class ScreenMoodle {

	private ArrayList<User> listaUsuario;

	public ScreenMoodle() {

	}

	public ScreenMoodle(ArrayList<User> listaUsuario) {
		super();
		this.listaUsuario = listaUsuario;
	}

	/*
	 * Formar lista usuarios
	 */
	public String[] ListUser() {
		int tamLista = this.listaUsuario.size();
		String[] nameUsers = new String[tamLista];
		for (int i = 0; i < tamLista; i++) {
			nameUsers[i] = this.listaUsuario.get(i).getName();
		}

		return nameUsers;
	}

	/*
	 * Listar usuarios por id Recogemos los ids que ha metido el usuario en la
	 * caja de texto
	 */
	@SuppressWarnings("null")
	public MultipartEntity recogerId(EditText mTexto, MultipartEntity entity) {
		try {
			String ids = mTexto.getText().toString();
			StringTokenizer token = new StringTokenizer(ids, ",");
			String idParam = null;
			int i = 0;
			entity = new MultipartEntity();
			while (token.hasMoreTokens()) {
				idParam = token.nextToken();
				if(isNumeric(idParam))
					entity.addPart("userids[" + i + "]", new StringBody(idParam));
				i++;
			}

		} catch (UnsupportedEncodingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}
		return entity;
	}
	
	private static boolean isNumeric(String cadena){
		try {
			Integer.parseInt(cadena);
			return true;
		} catch (NumberFormatException nfe){
			return false;
		}
	}

	/*
	 * Eliminar usuarios. Cogemos el id que hemos seleccionado para eliminarlo
	 */
	public MultipartEntity deleteUser(MultipartEntity entity) {
		try {
			UserSingleton usuario = UserSingleton.getInstance();
			
			entity.addPart("userids[0]", new StringBody(usuario.getId()));
		} catch (UnsupportedEncodingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		}
		return entity;
	}

	/*
	 * Modificar usuarios Cogemos el objeto que se muestra en pantalla y lo
	 * convertimos en parametros de entrada
	 */
	public MultipartEntity editarUser(MultipartEntity entity) {
		try {
			UserSingleton usuario = UserSingleton.getInstance();

			entity.addPart("users[0][id]", new StringBody(usuario.getId()));
			entity.addPart("users[0][username]", new StringBody(usuario.getName()));
			entity.addPart("users[0][password]", new StringBody("Moodle2010#"));
			entity.addPart("users[0][firstname]", new StringBody(usuario.getFirstName()));
			entity.addPart("users[0][lastname]", new StringBody(usuario.getLastName()));
			entity.addPart("users[0][email]", new StringBody(usuario.getEmail()));
			entity.addPart("users[0][customfields][0][type]", new StringBody(usuario.getCustomFields()));
			entity.addPart("users[0][customfields][0][value]", new StringBody(usuario.getCustomFields()));
		} catch (UnsupportedEncodingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		return entity;
	}

	/*
	 * Crear usuarios Cogemos el objeto que se muestra en pantalla y lo
	 * convertimos en parametros de entrada
	 */
	public MultipartEntity crearUser(MultipartEntity entity) {
		try {
			UserSingleton usuario = UserSingleton.getInstance();

			entity.addPart("users[0][username]", new StringBody(usuario.getName()));
			entity.addPart("users[0][password]", new StringBody(usuario.getPass()));
			entity.addPart("users[0][firstname]", new StringBody(usuario.getFirstName()));
			entity.addPart("users[0][lastname]", new StringBody(usuario.getLastName()));
			entity.addPart("users[0][email]", new StringBody(usuario.getEmail()));
			entity.addPart("users[0][auth]", new StringBody("manual"));
			entity.addPart("users[0][idnumber]", new StringBody(""));
			entity.addPart("users[0][emailstop]", new StringBody("0"));
			entity.addPart("users[0][lang]", new StringBody("es"));
		} catch (UnsupportedEncodingException e) {
			// Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchElementException e) {
			e.printStackTrace();
		}

		return entity;
	}
}
