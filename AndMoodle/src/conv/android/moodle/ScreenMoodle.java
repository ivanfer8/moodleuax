package conv.android.moodle;

import java.util.ArrayList;
import java.util.Iterator;

import fac.android.moodle.User;

public class ScreenMoodle {
	
	private ArrayList<User> listaUsuario;
	
	public ScreenMoodle(ArrayList<User> listaUsuario){
		super();
		this.listaUsuario = listaUsuario;
	}
	
	public String[] ListUser(){
		int tamLista = this.listaUsuario.size();
		String[] nameUsers = new String[tamLista];
		for(int i=0;i<tamLista;i++)
		{
			nameUsers[i] = this.listaUsuario.get(i).getName();
		}
		
		return nameUsers;
	}

}
