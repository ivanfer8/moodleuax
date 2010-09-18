package uax.android.moodle;

import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import conv.android.moodle.CallWebService;
import conv.android.moodle.ErrorConverter;
import conv.android.moodle.MyUserConverter;
import conv.android.moodle.ScreenMoodle;
import fac.android.moodle.ErrorException;
import fac.android.moodle.User;
import fac.android.moodle.UserSingleton;

public class UserMenu extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<User> usuario = null;
	private OrderAdapter m_adapter;
	private Runnable viewOrders;
	private String funcion = "moodle_user_get_users_by_id";
	private ErrorException error = new ErrorException();
	private MultipartEntity entity = new MultipartEntity();

	private ImageButton mBuscar = null;
	private EditText mTexto = null;
	private SharedPreferences preferences;
	private String token;
	private String host;

	private static final int REQST_USEREDIT = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		usuario = new ArrayList<User>();
		this.m_adapter = new OrderAdapter(this, R.layout.row, usuario);
		setListAdapter(this.m_adapter);
		/*
		 * Comienzan las acciones
		 */

		mBuscar = (ImageButton) findViewById(R.id.ok);
		mTexto = (EditText) findViewById(R.id.paramToken);
		// Inicializamos las preferencias, el token del usuario
		preferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.token = preferences.getString("token", "n/a");
		this.host = preferences.getString("host", "n/a");

		/*
		 * Pulsamos la lupa para buscar usuarios
		 */
		mBuscar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				viewOrders = new Runnable() {
					@Override
					public void run() {
						usuario = new ArrayList<User>();
						usuario.removeAll(usuario);
						entity = (new ScreenMoodle()).recogerId(mTexto, entity);
						listUserById(funcion, entity);
					}
				};
				try {
					Thread thread = new Thread(null, viewOrders, "MagentoBackground");
					thread.start();
					m_adapter.clear();
					m_ProgressDialog = ProgressDialog.show(UserMenu.this, "Por favor, espere...", "Buscando usuarios ...", true);
				} catch (Exception e) {
					// Auto-generated catch block
					mostrarException(e.getMessage());
				}

			}
		});
	}

	// This method is called once the menu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent i = null;
		switch (item.getItemId()) {
		// We have only one menu option
		case R.id.tokenUsu:
			// Launch Preference activity
			i = new Intent(UserMenu.this, Preferences.class);
			startActivity(i);
			// A toast is a view containing a quick little message for the user.
			Toast.makeText(UserMenu.this, "Aquí puedes cambiar las preferencias de usuario.", Toast.LENGTH_LONG).show();
			break;
			
		case R.id.userCreate:
			// Launch Preference activity
			i = new Intent(UserMenu.this, UserCreate.class);
			startActivity(i);
			// A toast is a view containing a quick little message for the user.
			Toast.makeText(UserMenu.this, "Aquí puedes crear nuevos usuarios.", Toast.LENGTH_LONG).show();
			break;

		}
		return true;
	}

	/*
	 * Seleccionar un elemento de la lista
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView,
	 * android.view.View, int, long)
	 */
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Lanzamos la nueva actividad y un mensaje con el id seleccionado
		UserSingleton usuSelecc = UserSingleton.getInstance();
		usuSelecc.setId(usuario.get(position).getId());
		usuSelecc.setName(usuario.get(position).getName());
		usuSelecc.setFirstName(usuario.get(position).getFirstName());
		usuSelecc.setLastName(usuario.get(position).getLastName());
		usuSelecc.setEmail(usuario.get(position).getEmail());
		usuSelecc.setAuth(usuario.get(position).getAuth());
		usuSelecc.setConfirmed(usuario.get(position).getConfirmed());
		usuSelecc.setIdNumber(usuario.get(position).getIdNumber());
		usuSelecc.setEmailStop(usuario.get(position).getEmailStop());
		usuSelecc.setLang(usuario.get(position).getLang());
		usuSelecc.setTheme(usuario.get(position).getTheme());
		usuSelecc.setTimeZone(usuario.get(position).getTimeZone());
		usuSelecc.setMailFormat(usuario.get(position).getMailFormat());
		usuSelecc.setDescription(usuario.get(position).getDescription());
		usuSelecc.setCity(usuario.get(position).getCity());
		usuSelecc.setCountry(usuario.get(position).getCountry());
		usuSelecc.setCustomFields(usuario.get(position).getCustomFields());
		Intent i = new Intent();
		i.setClass(UserMenu.this, UserEdit.class);
		startActivityForResult(i, REQST_USEREDIT);
		Toast.makeText(getApplicationContext(), "Ha seleccionado " + ((User) l.getItemAtPosition(position)).getName(), Toast.LENGTH_SHORT).show();

	}

	/*
	 * Menu para añadir el token del usuario
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	/*
	 * Recargar lista
	 */
	private Runnable returnRes = new Runnable() {

		@Override
		public void run() {
			if (usuario != null && usuario.size() > 0) {
				m_adapter.notifyDataSetChanged();
				for (int i = 0; i < usuario.size(); i++)
					m_adapter.add(usuario.get(i));
			}
			m_ProgressDialog.dismiss();
			m_adapter.notifyDataSetChanged();
		}
	};

	/*
	 * Lanzar web service con las opciones elegidas
	 */
	@SuppressWarnings("unchecked")
	private void listUserById(String moodleWebService, MultipartEntity entity) {
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(this.token, moodleWebService, this.host, entity);

			String xml = cws.Consume();
			CharSequence exception = "EXCEPTION";

			// comprobamos que no haya un error
			if (!xml.substring(0, 4).equals("ERROR") && !xml.contains(exception)) {
				// no hay error
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new MyUserConverter());
				xstream.alias("RESPONSE", User.class);
				usuario = (ArrayList<User>) xstream.fromXML(xml);
			} else if (xml.contains(exception)) {
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new ErrorConverter());
				xstream.alias("EXCEPTION", ErrorException.class);
				error = (ErrorException) xstream.fromXML(xml);
			} else {
				mostrarException("Se ha producido una excepción.");
			}

			// return listUser;

		} catch (Exception e) {
			// Auto-generated catch block
			mostrarException(e.getMessage());
			// return null;
		}
		runOnUiThread(returnRes);
	}

	/*
	 * Pintar la lista
	 */
	private class OrderAdapter extends ArrayAdapter<User> {

		private ArrayList<User> items;

		public OrderAdapter(Context context, int textViewResourceId, ArrayList<User> items) {
			super(context, textViewResourceId, items);
			this.items = items;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View v = convertView;
			if (v == null) {
				LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				v = vi.inflate(R.layout.row, null);
			}
			User o = items.get(position);
			if (o != null) {
				TextView tt = (TextView) v.findViewById(R.id.toptext);
				TextView bt = (TextView) v.findViewById(R.id.bottomtext);
				if (tt != null) {
					tt.setText("Id Usuario: " + o.getId());
				}
				if (bt != null) {
					bt.setText("Nombre: " + o.getName());
				}
			}
			return v;
		}
	}
	
	private void mostrarException(String descError) {
		this.error.setDescError(descError);
		runOnUiThread(returnRes);
	}

}