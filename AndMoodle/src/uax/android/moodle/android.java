package uax.android.moodle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import conv.android.moodle.CallWebService;
import conv.android.moodle.ErrorConverter;
import conv.android.moodle.MyUserConverter;
import fac.android.moodle.ErrorException;
import fac.android.moodle.User;

public class android extends ListActivity {

	private ProgressDialog m_ProgressDialog = null;
	private ArrayList<User> usuario = null;
	private OrderAdapter m_adapter;
	private Runnable viewOrders;
	private String token = "4ae93a61942f8a07e5f738281807819a";
	private String funcion = "moodle_user_get_users_by_id";
	private ErrorException error = new ErrorException();
	private MultipartEntity entity = new MultipartEntity();

	private ImageButton mBuscar = null;
	private EditText mTexto = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//quitar barra superior
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		
		setContentView(R.layout.main);
		usuario = new ArrayList<User>();
		this.m_adapter = new OrderAdapter(this, R.layout.row, usuario);
		setListAdapter(this.m_adapter);
		/*
		 * Comienzan las acciones
		 */

		mBuscar = (ImageButton) findViewById(R.id.ok);
		mTexto = (EditText) findViewById(R.id.token);

		/*
		 * Pulsamos la lupa para buscar usuarios
		 */
		mBuscar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				viewOrders = new Runnable() {
					@Override
					public void run() {
						try {
							usuario = new ArrayList<User>();
							entity.addPart("userids[0]", new StringBody("2"));
							listUserById(token, funcion, entity);
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				};
				Thread thread = new Thread(null, viewOrders, "MagentoBackground");
				thread.start();
				m_ProgressDialog = ProgressDialog.show(android.this, "Please wait...", "Retrieving data ...", true);

			}
		});

	}
	
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		// Get the item that was clicked
		Toast.makeText(getApplicationContext(), "You have clicked on " + ((User)l.getItemAtPosition(position)).getId(), Toast.LENGTH_SHORT).show();

	}


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

	@SuppressWarnings("unchecked")
	private void listUserById(String token, String moodleWebService, MultipartEntity entity) {
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(token, moodleWebService, entity);

			String xml = cws.Consume();
			CharSequence exception = "EXCEPTION";

			// comprobamos que no haya un error
			if (!xml.substring(0, 4).equals("ERROR") && !xml.contains(exception)) {
				// no hay error
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new MyUserConverter());
				xstream.alias("RESPONSE", User.class);
				usuario = (ArrayList<User>) xstream.fromXML(xml);
				Log.i("--ARRAY", "" + usuario.size());
			} else if (xml.contains(exception)) {
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new ErrorConverter());
				xstream.alias("MESSAGE", ErrorException.class);
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
					tt.setText("Name: " + o.getId());
				}
				if (bt != null) {
					bt.setText("Status: " + o.getName());
				}
			}
			return v;
		}
	}
}