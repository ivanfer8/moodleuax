package uax.android.moodle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import conv.android.moodle.MyUserConverter;
import fac.android.moodle.User;

public class android extends Activity {
	/** Called when the activity is first created. */

	private Button mBuscar = null;
	private EditText mTexto = null;
	private final String DEBUG_TAG = "httpPostExample";

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
					ArrayList<User> usuario = (ArrayList<User>) listUserById(
							mTexto.getText().toString(),
							"moodle_user_get_users_by_id");
					EditText nombreUsu = (EditText) findViewById(R.id.nombre);
					if (!usuario.equals(null))
						nombreUsu.setText(usuario.get(0).getName());
					else {
						nombreUsu.setBackgroundColor(Color.RED);
						nombreUsu.setText("Error");
					}
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block e.printStackTrace();
				}
			}
		});

	}

	public ArrayList<User> listUserById(String token, String moodleWebService)
			throws UnsupportedEncodingException {
		// WebService list_user_By_Id
		try {

			HttpClient httpClient = new DefaultHttpClient();

			HttpPost request = new HttpPost(
					"http://192.168.1.34/moodle/webservice/rest/server.php?wstoken=4ae93a61942f8a07e5f738281807819a&wsfunction=moodle_user_get_users_by_id");
			MultipartEntity entity = new MultipartEntity();
			entity.addPart("userids[0]", new StringBody("1"));
			request.setEntity(entity);

			String xml = "";
			try {
				HttpResponse response = httpClient.execute(request);
				int status = response.getStatusLine().getStatusCode();

				if (status != HttpStatus.SC_OK) {
					ByteArrayOutputStream ostream = new ByteArrayOutputStream();
					response.getEntity().writeTo(ostream);
					Log.e("HTTP CLIENT", ostream.toString());
				} else {
					InputStream content = response.getEntity().getContent();
					// <consume response>
					xml = readFromBuffer(new BufferedReader(
							new InputStreamReader(content)));
					content.close(); // this will also close the connection
				}
			} catch (ClientProtocolException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			XStream xstream = new XStream(new DomDriver());
			xstream.registerConverter(new MyUserConverter());
			xstream.alias("RESPONSE", User.class);

			ArrayList<User> listUser = (ArrayList<User>) xstream.fromXML(xml);

			System.out.println(listUser.get(0).getName());

			return listUser;

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			System.out.println("ERROR: " + e.getMessage());
			return null;
		}
	}

	private String readFromBuffer(BufferedReader br) {
		StringBuilder text = new StringBuilder();
		try {
			String line;
			while ((line = br.readLine()) != null) {
				text.append(line);
				text.append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
			// tratar excepción!!!
		}
		return text.toString();
	}

}