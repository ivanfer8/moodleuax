package uax.android.moodle;

import java.util.ArrayList;

import javax.ws.rs.core.MultivaluedMap;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import fac.android.moodle.User;



// ?wstoken=69910803eb212ddbc3eccd4b11155b94&wsfunction=moodle_user_get_users_by_id

public class UserList extends Activity {

	// This token must be created by the user in the Web Services plugin
	private static String token = "4ae93a61942f8a07e5f738281807819a";

	private static String moodleWebService = "moodle_user_get_users_by_id";

	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			Client client = Client.create();
			
			WebResource webResource = client.resource("http://localhost/moodle/webservice/rest/server.php");

			MultivaluedMap<String, String> urlParams = new MultivaluedMapImpl();
			urlParams.add("wstoken", token);
			urlParams.add("wsfunction", moodleWebService);

			MultivaluedMap<String, String> formData = new MultivaluedMapImpl();
			formData.add("userids[0]","1");
			formData.add("userids[1]","2");
			
			ClientResponse response = webResource.queryParams(urlParams).type("application/x-www-form-urlencoded").post(ClientResponse.class, formData);
			
			String xml = response.getEntity(String.class);
			
			XStream xstream = new XStream(new DomDriver());
			xstream.registerConverter(new UserConverter());
			xstream.alias("RESPONSE",User.class);

			ArrayList<User> listUser = (ArrayList<User>) xstream.fromXML(xml);

			/*System.out.println(listUser.get(0).getName());
			System.out.println(listUser.get(1).getName());*/
			TextView textview = new TextView(this);
	        textview.setText(listUser.get(0).getName());
	        setContentView(textview);
			
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.err.println(e.toString());
		}
		
	}
}
