package uax.android.moodle;

import org.apache.http.entity.mime.MultipartEntity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import conv.android.moodle.CallWebService;
import conv.android.moodle.ErrorConverter;
import conv.android.moodle.ScreenMoodle;
import fac.android.moodle.ErrorException;
import fac.android.moodle.UserSingleton;

public class UserCreate extends Activity {

	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewOrders;
	private SharedPreferences preferences;
	private String host;
	private String token;
	private ErrorException error = new ErrorException();
	private MultipartEntity entity = new MultipartEntity();

	private String funcion = "moodle_user_create_users";

	private UserSingleton usuSelecc;
	private EditText etNombre = null;
	private EditText etPApellido = null;
	private EditText etSApellido = null;
	private EditText etCorreo = null;
	private EditText etPass = null;

	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.usercreate);

		// Inicializamos las preferencias, el token del usuario
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.token = preferences.getString("token", "n/a");
		this.host = preferences.getString("host", "n/a");

		usuSelecc = UserSingleton.getInstance();
		
		etNombre = (EditText) findViewById(R.id.editNombre);
		etPApellido = (EditText) findViewById(R.id.editPApellido);
		etSApellido = (EditText) findViewById(R.id.editPSapellido);
		etCorreo = (EditText) findViewById(R.id.editCorreo);
		etPass = (EditText) findViewById(R.id.editPass);
	}

	/*
	 * Menu para añadir el token del usuario
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.usercreate, menu);
		return true;
	}

	// This method is called once the menu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// We have only one menu option
		case R.id.userCreate:
			// Lanzamos la edicion de usuarios
			viewOrders = new Runnable() {
				@Override
				public void run() {
					// aqui la acción
					recogerDatos();
					entity = (new ScreenMoodle()).editarUser(entity);
					userCreate(funcion, entity);
				}
			};
			Thread thread = new Thread(null, viewOrders, "MagentoBackground");
			thread.start();
			m_ProgressDialog = ProgressDialog.show(UserCreate.this, "Por favor, espere...", "Creando usuario ...", true);

			break;

		}
		return true;
	}

	/*
	 * Muestra mensaje de ok o ko
	 */
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			try {
				if (error.getDescError().length() > 0) {
					// Se ha producido un error
					Toast.makeText(UserCreate.this, "Se ha producido un error.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(UserCreate.this, "Usuario modificado correctamente.", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// El mensaje de error esta vacio
				Toast.makeText(UserCreate.this, "Usuario modificado correctamente.", Toast.LENGTH_LONG).show();
				//m_ProgressDialog.dismiss();
			}
			m_ProgressDialog.dismiss();
		}
	};

	// lanzamos el evento para editar los usuarios
	private void userCreate(String moodleWebService, MultipartEntity entity) {
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(this.token, moodleWebService, this.host, entity);

			String xml = cws.Consume();
			CharSequence exception = "EXCEPTION";

			// comprobamos que no haya un error
			if (!xml.substring(0, 4).equals("ERROR") && !xml.contains(exception)) {
				// no hay error

			} else if (xml.contains(exception)) {
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new ErrorConverter());
				xstream.alias("EXCEPTION", ErrorException.class);
				error = (ErrorException) xstream.fromXML(xml);
			} else {
				error.setDescError(xml);
			}

		} catch (Exception e) {
			// Auto-generated catch block
			error.setDescError(e.getMessage());
			// return null;
		}
		runOnUiThread(returnRes);
	}

	/*
	 * Recogemos los datos que se muestran por pantalla
	 */
	private void recogerDatos() {
		usuSelecc.setName(etNombre.getText().toString());
		usuSelecc.setFirstName(etPApellido.getText().toString());
		usuSelecc.setLastName(etSApellido.getText().toString());
		usuSelecc.setEmail(etCorreo.getText().toString());
		usuSelecc.setPass(etPass.getText().toString());
	}
}
