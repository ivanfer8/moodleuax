package uax.android.moodle;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import fac.android.moodle.User;

public class UserEdit extends Activity {
	
	private EditText idUsu = null;
	private User usuario;
	
	/*
	 * Constructor
	 */
	public UserEdit(User usuario)
	{
		this.usuario = usuario;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.useredit);
		
		idUsu = (EditText) findViewById(R.id.idUsu);
		idUsu.setText("abel");
	}
}
