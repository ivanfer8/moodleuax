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
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

public class Android extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<User> usuario = null;
	private OrderAdapter m_adapter;
	private Runnable viewOrders;
	private String token;
	private String funcion = "moodle_user_get_users_by_id";
	private ErrorException error = new ErrorException();
	private MultipartEntity entity = new MultipartEntity();

	private ImageButton mBuscar = null;
	private Button pButton = null;
	private EditText mTexto = null;
	private SharedPreferences preferences;
	private String host = null;
	
	private static final int REQST_USEREDIT = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// quitar barra superior
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
						entity = (new ScreenMoodle()).recogerId(mTexto,entity);
						listUserById(token, funcion, entity);
					}
				};
				Thread thread = new Thread(null, viewOrders, "MagentoBackground");
				thread.start();
				m_ProgressDialog = ProgressDialog.show(Android.this, "Por favor, espere...", "Buscando usuarios ...", true);

			}
		});

		/*
		 * Pulsamos las preferencias para cambiar el token
		 */

		pButton = (Button) findViewById(R.id.Button01);
		this.pButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String token = preferences.getString("token", "n/a");
				String host = preferences.getString("host", "n/a");
				Toast.makeText(Android.this, "Has cambiado token: " + token + " host: "+host, Toast.LENGTH_LONG).show();

			}
		});

	}

	// This method is called once the menu is selected
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// We have only one menu option
		case R.id.tokenUsu:
			// Launch Preference activity
			Intent i = new Intent(Android.this, Preferences.class);
			startActivity(i);
			// A toast is a view containing a quick little message for the user.
			Toast.makeText(Android.this, "Aquí puedes cambiar las preferencias de usuario.", Toast.LENGTH_LONG).show();
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
		//
		Intent i = new Intent();
		i.setClass(Android.this, UserEdit.class);
		i.putParcelableArrayListExtra("usuario", this.usuario);
		//i.putExtra("usuario", (User) l.getItemAtPosition(position));
		startActivityForResult(i,REQST_USEREDIT);
		Toast.makeText(getApplicationContext(), "Ha seleccionado " + ((User) l.getItemAtPosition(position)).getId(), Toast.LENGTH_SHORT).show();

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
	private void listUserById(String token, String moodleWebService, MultipartEntity entity) {
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(token, moodleWebService,host, entity);

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
				error.setDescError(xml);
			}

			// return listUser;

		} catch (Exception e) {
			// Auto-generated catch block
			error.setDescError(e.getMessage());
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

}