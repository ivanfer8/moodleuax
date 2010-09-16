package uax.android.moodle;

import org.apache.http.entity.mime.MultipartEntity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
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

public class UserCreate extends Activity {

	private ProgressDialog m_ProgressDialog = null;
	private Runnable viewOrders;
	private SharedPreferences preferences;
	private String host;
	private String token;
	private ErrorException error = new ErrorException();
	private MultipartEntity entity = new MultipartEntity();
	private Button mButton = null;

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
		
		mButton = (Button)findViewById(R.id.buttonCrear);

		usuSelecc = UserSingleton.getInstance();
		
		etNombre = (EditText) findViewById(R.id.editNombre);
		etPApellido = (EditText) findViewById(R.id.editPApellido);
		etSApellido = (EditText) findViewById(R.id.editPSapellido);
		etCorreo = (EditText) findViewById(R.id.editCorreo);
		etPass = (EditText) findViewById(R.id.editPass);
		
		/*
		 * Pulsamos el boton para crear usuarios
		 */
		mButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				viewOrders = new Runnable() {
					@Override
					public void run() {
						// aqui la acción
						recogerDatos();
						entity = (new ScreenMoodle()).crearUser(entity);
						if((new CheckSrcreen()).checkCreateUser())
							userCreate(funcion, entity);
						else
							mostrarException("Campos no válidos");
					}
				};
				Thread thread = new Thread(null, viewOrders, "MagentoBackground");
				thread.start();
				m_ProgressDialog = ProgressDialog.show(UserCreate.this, "Por favor, espere...", "Creando usuario ...", true);

			}
		});
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
				mostrarException(error.getDescError());
			}

		} catch (Exception e) {
			// Auto-generated catch block
			mostrarException(e.getMessage());
			// return null;
		}
		runOnUiThread(returnRes);
	}

	/*
	 * Recogemos los datos que se muestran por pantalla
	 */
	private void recogerDatos() {
		try {
			usuSelecc.setName(etNombre.getText().toString());
			usuSelecc.setFirstName(etPApellido.getText().toString());
			usuSelecc.setLastName(etSApellido.getText().toString());
			usuSelecc.setEmail(etCorreo.getText().toString());
			usuSelecc.setPass(etPass.getText().toString());
		} catch (Exception e) {
			//Auto-generated catch block
			mostrarException(e.getMessage());
		}
	}
	
	private void mostrarException(String error){
		Toast.makeText(UserCreate.this, error, Toast.LENGTH_LONG).show();
	}
}
