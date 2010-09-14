package conv.android.moodle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.widget.EditText;
import fac.android.moodle.User;

public class ScreenMoodle {
	
	private ArrayList<User> listaUsuario;
	
	public ScreenMoodle()
	{
		
	}
	
	public ScreenMoodle(ArrayList<User> listaUsuario){
		super();
		this.listaUsuario = listaUsuario;
	}
	
	/*
	 * Formar lista usuarios
	 */
	public String[] ListUser(){
		int tamLista = this.listaUsuario.size();
		String[] nameUsers = new String[tamLista];
		for(int i=0;i<tamLista;i++)
		{
			nameUsers[i] = this.listaUsuario.get(i).getName();
		}
		
		return nameUsers;
	}

	/*
	 * Recogemos los ids que ha metido el usuario en la caja de texto
	 */
	public MultipartEntity recogerId(EditText mTexto,MultipartEntity entity) {
		try {
			String ids = mTexto.getText().toString();
			StringTokenizer token = new StringTokenizer(ids, ",");
			int i = 0;
			while (token.hasMoreTokens()) {
				entity.addPart("userids[" + i +"]", new StringBody(token.nextToken()));
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

}
