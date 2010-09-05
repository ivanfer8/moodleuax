package uax.android.moodle;

import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import conv.android.moodle.MyUserConverter;
import fac.android.moodle.User;

public class android extends Activity {
    /** Called when the activity is first created. */
	
	private Button mBuscar = null;
	private EditText mTexto = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.main);
	    
	    mBuscar = (Button)findViewById(R.id.buscar);
		mTexto = (EditText)findViewById(R.id.token);
		
		mTexto.setTextColor(Color.BLUE);
		mTexto.setText("4ae93a61942f8a07e5f738281807819a");
		
		mBuscar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<User> usuario = (ArrayList<User>) listUserById(mTexto.getText().toString(), "moodle_user_get_users_by_id");
				EditText nombreUsu = (EditText)findViewById(R.id.nombre);
				if(!usuario.equals(null))
					nombreUsu.setText(usuario.get(0).getName());
				else
				{
					nombreUsu.setBackgroundColor(Color.RED);
					nombreUsu.setText("Error");
				}
			}
		});
		
	}
	
	public ArrayList<User> listUserById(String token, String moodleWebService){
		//WebService list_user_By_Id
		try {
			Client client = Client.create();
			
			WebResource webResource = client.resource("http://127.0.0.1/moodle/webservice/rest/server.php");

			MultivaluedMap<String, String> urlParams = new MultivaluedMapImpl();
			urlParams.add("wstoken", token);
			urlParams.add("wsfunction", moodleWebService);

			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("userids[0]","1");
			
			ClientResponse response = webResource.queryParams(urlParams).post(ClientResponse.class, formData);
			
			/*String xml = response.getEntity(String.class);
			
			XStream xstream = new XStream(new DomDriver());
			xstream.registerConverter(new MyUserConverter());
			xstream.alias("RESPONSE",User.class);

			ArrayList<User> listUser = (ArrayList<User>) xstream.fromXML(xml);

			System.out.println(listUser.get(0).getName());
			
			return listUser;*/
			return null;
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.toString());
			return null;
		}		
	}

}