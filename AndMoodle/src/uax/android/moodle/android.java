package uax.android.moodle;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import conv.android.moodle.CallWebService;
import conv.android.moodle.ErrorConverter;
import conv.android.moodle.MyUserConverter;
import fac.android.moodle.ErrorException;
import fac.android.moodle.User;

public class android extends Activity {
	/** Called when the activity is first created. */

	private Button mBuscar = null;
	private EditText mTexto = null;
	private MultipartEntity entity = new MultipartEntity();
	private ErrorException error = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mBuscar = (Button) findViewById(R.id.buscar);
		mTexto = (EditText) findViewById(R.id.token);

		mTexto.setTextColor(Color.BLUE);
		mTexto.setText("4ae93a61942f8a07e5f738281807819a");

		mBuscar.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					entity.addPart("userids[0]", new StringBody("a"));
					ArrayList<User> usuario = (ArrayList<User>) listUserById(
							mTexto.getText().toString(),
							"moodle_user_get_users_by_id", entity);
					EditText nombreUsu = (EditText) findViewById(R.id.nombre);
					if (!usuario.equals(null))
						nombreUsu.setText(usuario.get(0).getName());
					else {
						nombreUsu.setBackgroundColor(Color.RED);
						nombreUsu.setText(error.getDescError());
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block e.printStackTrace();
				}
			}
		});

	}

	@SuppressWarnings("unchecked")
	public ArrayList<User> listUserById(String token, String moodleWebService,
			MultipartEntity entity){
		// WebService list_user_By_Id
		try {

			CallWebService cws = new CallWebService(token, moodleWebService,entity);

			String xml = cws.Consume();
			ArrayList<User> listUser = null;
			CharSequence exception = "EXCEPTION";
			boolean aux = xml.contains(exception);

			// comprobamos que no haya un error
			if (!xml.substring(0, 4).equals("ERROR")&&!xml.contains(exception)) {
				// no hay error
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new MyUserConverter());
				xstream.alias("RESPONSE", User.class);
				listUser = (ArrayList<User>) xstream.fromXML(xml);
			}
			else if(xml.contains(exception))
			{
				XStream xstream = new XStream(new DomDriver());
				xstream.registerConverter(new ErrorConverter());
				xstream.alias("EXCEPTION", ErrorConverter.class);
				error = (ErrorException) xstream.fromXML(xml);
			}
			else
			{
				error.setDescError(xml);
			}

			return listUser;

		} catch (Exception e) {
			// Auto-generated catch block
			error.setDescError(e.getMessage());
			return null;
		}
	}
}