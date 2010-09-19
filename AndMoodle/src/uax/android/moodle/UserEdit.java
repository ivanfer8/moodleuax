package uax.android.moodle;

import org.apache.http.entity.mime.MultipartEntity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import conv.android.moodle.CheckSrcreen;
import conv.android.moodle.ErrorConverter;
import conv.android.moodle.ScreenMoodle;
import fac.android.moodle.ErrorException;
import fac.android.moodle.UserSingleton;

public class UserEdit extends Activity {

	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewOrders;
	private SharedPreferences preferences;
	private String host;
	private String token;
	private ErrorException error = new ErrorException();
	private MultipartEntity entity = new MultipartEntity();

	private String funcionEdit = "moodle_user_update_users";
	private String funcionDelete = "moodle_user_delete_users";

	private UserSingleton usuSelecc;
	private EditText etId = null;
	private EditText etNombre = null;
	private EditText etPApellido = null;
	private EditText etSApellido = null;
	private EditText etCorreo = null;
	private EditText etAuth = null;
	private EditText etLang = null;
	private EditText etDesc = null;
	private EditText etCity = null;
	private EditText etCountry = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.useredit);

		// Inicializamos las preferencias, el token del usuario
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.token = preferences.getString("token", "n/a");
		this.host = preferences.getString("host", "n/a");

		usuSelecc = UserSingleton.getInstance();
		// Id
		etId = (EditText) findViewById(R.id.editId);
		etId.setText(usuSelecc.getId());
		// Nombre
		etNombre = (EditText) findViewById(R.id.editNombre);
		etNombre.setText(usuSelecc.getName());
		// Primer apellido
		etPApellido = (EditText) findViewById(R.id.editPApellido);
		etPApellido.setText(usuSelecc.getFirstName());
		// Segundo apellido
		etSApellido = (EditText) findViewById(R.id.editPSapellido);
		etSApellido.setText(usuSelecc.getLastName());
		// Correo electronico
		etCorreo = (EditText) findViewById(R.id.editCorreo);
		etCorreo.setText(usuSelecc.getEmail());
		// Autorizacion
		etAuth = (EditText) findViewById(R.id.editAuth);
		etAuth.setText(usuSelecc.getAuth());
		// Lenguaje
		etLang = (EditText) findViewById(R.id.editLang);
		etLang.setText(usuSelecc.getLang());
		// Descripcion
		etDesc = (EditText) findViewById(R.id.editDesc);
		etDesc.setText(usuSelecc.getDescription());
		// Ciudad
		etCity = (EditText) findViewById(R.id.editCity);
		etCity.setText(usuSelecc.getCity());
		// País
		etCountry = (EditText) findViewById(R.id.editCountry);
		etCountry.setText(usuSelecc.getCountry());
	}

	/*
	 * Menu para añadir el token del usuario
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.usermenu, menu);
		return true;
	}

	// This method is called once the menu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		;
		switch (item.getItemId()) {
		// We have only one menu option
		case R.id.userEdit:
			// Lanzamos la edicion de usuarios
			viewOrders = new Runnable() {
				@Override
				public void run() {
					// aqui la acción
					recogerDatos();
					entity = (new ScreenMoodle()).editarUser(entity);
					if ((new CheckSrcreen()).checkEditUser()) {
						userUpdate(funcionEdit, entity);
					} else {
						// datos incorrectos
						mostrarException("Error en los datos de entrada");
					}
				}
			};
			Thread thread = new Thread(null, viewOrders, "MagentoBackground");
			thread.start();
			m_ProgressDialog = ProgressDialog.show(UserEdit.this, "Por favor, espere...", "Modificando usuario ...", true);

			break;

		case R.id.userDelete:
			// lanzamos eliminar usuario
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			alertDialog.setTitle("Elimiar...");
			alertDialog.setMessage("¿Estás seguro?");
			alertDialog.setButton("Si", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					// here you can add functions
					// procedemos a elimiar el usuario
					viewOrders = new Runnable() {
						@Override
						public void run() {
							// aqui la acción
							recogerDatos();
							entity = (new ScreenMoodle()).deleteUser(entity);
							userDelete(funcionDelete, entity);
						}
					};
					Thread thread = new Thread(null, viewOrders, "MagentoBackground");
					thread.start();
					m_ProgressDialog = ProgressDialog.show(UserEdit.this, "Por favor, espere...", "Eliminando usuario ...", true);
				}
			});
			alertDialog.setIcon(R.drawable.icon);
			alertDialog.show();
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
					Toast.makeText(UserEdit.this, "Se ha producido un error.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(UserEdit.this, "Usuario modificado correctamente.", Toast.LENGTH_LONG).show();
				}
			} catch (Exception e) {
				// El mensaje de error esta vacio
				Toast.makeText(UserEdit.this, "Acción realizada correctamente.", Toast.LENGTH_LONG).show();
				// m_ProgressDialog.dismiss();
			}
			m_ProgressDialog.dismiss();
		}
	};

	// lanzamos el evento para editar los usuarios
	private void userUpdate(String moodleWebService, MultipartEntity entity) {
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(this.token, moodleWebService, this.host, entity);

			String xml = cws.Consume();
			CharSequence exception = "EXCEPTION";

			// comprobamos que no haya un error
			if (!xml.substring(0, 4).equals("ERROR") && !xml.contains(exception)) {
				// no hay error
				finish();
			} else if (xml.contains(exception)) {
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new ErrorConverter());
				xstream.alias("EXCEPTION", ErrorException.class);
				error = (ErrorException) xstream.fromXML(xml);
			} else {
				error.setDescError(xml);
				Toast.makeText(UserEdit.this, error.getDescError(), Toast.LENGTH_LONG).show();
			}

		} catch (Exception e) {
			// Auto-generated catch block
			mostrarException(e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	// lanzamos el evento para eliminar los usuarios
	private void userDelete(String moodleWebService, MultipartEntity entity) {
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(this.token, moodleWebService, this.host, entity);

			String xml = cws.Consume();
			CharSequence exception = "EXCEPTION";

			// comprobamos que no haya un error
			if (!xml.substring(0, 4).equals("ERROR") && !xml.contains(exception)) {
				// no hay error
				finish();
			} else if (xml.contains(exception)) {
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new ErrorConverter());
				xstream.alias("EXCEPTION", ErrorException.class);
				error = (ErrorException) xstream.fromXML(xml);
			} else {
				mostrarException("Error eliminado usuario");
			}

		} catch (Exception e) {
			// Auto-generated catch block
			mostrarException(e.getMessage());
		}
		runOnUiThread(returnRes);
	}

	/*
	 * Recogemos los datos que se muestran por pantalla
	 */
	private void recogerDatos() {
		try {
			usuSelecc.setId(etId.getText().toString());
			usuSelecc.setName(etNombre.getText().toString());
			usuSelecc.setFirstName(etPApellido.getText().toString());
			usuSelecc.setLastName(etSApellido.getText().toString());
			usuSelecc.setEmail(etCorreo.getText().toString());
			usuSelecc.setCustomFields(etAuth.getText().toString());
		} catch (Exception e) {
			// Auto-generated catch block
			mostrarException(e.getMessage());
		}
	}
	
	private void mostrarException(String descError) {
		this.error.setDescError(descError);
		runOnUiThread(returnRes);
	}
}
