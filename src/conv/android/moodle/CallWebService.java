package conv.android.moodle;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class CallWebService {

	private String token;
	private String moodleWebService;
	private MultipartEntity entity;
	private String host;

	public CallWebService(String token, String moodleWebService,String host, MultipartEntity entity) {
		this.token = token;
		this.moodleWebService = moodleWebService;
		this.entity = entity;
		this.host = host;
	}

	public String Consume(){

		String responseXml = "";
		try {
			HttpClient httpClient = new DefaultHttpClient();
			//192.168.1.34
			ArrayList<String> url = new ArrayList<String>(3);
			url.add("http://"+host+"/moodle/webservice/rest/server.php?");
			url.add("wstoken="+token);
			url.add("&wsfunction="+moodleWebService);

			HttpPost request = new HttpPost(createUrl(url));
			request.setEntity(entity);

			HttpResponse response = httpClient.execute(request);
			int status = response.getStatusLine().getStatusCode();

			if (status != HttpStatus.SC_OK) {
				ByteArrayOutputStream ostream = new ByteArrayOutputStream();
				response.getEntity().writeTo(ostream);
				responseXml = "ERROR: "+ ostream.toString();
			} else {
				InputStream content = response.getEntity().getContent();
				// <consume response>
				responseXml = readFromBuffer(new BufferedReader(new InputStreamReader(content)));
				//cerramos la conexion
				content.close();
			}
			return responseXml;
		} catch (ClientProtocolException cpe) {
			// Auto-generated catch block
			return ("ERROR: "+cpe.getMessage());
		} catch (IOException ioe) {
			// Auto-generated catch block
			return ("ERROR:"+ioe.getMessage());
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
	
	private String createUrl(ArrayList<String> url)
	{
		//formamos una url con los valores del arrayList
		StringBuffer urlCompleta = new StringBuffer();
		for (Iterator<String> iterator = url.iterator(); iterator.hasNext();) {
			urlCompleta.append(iterator.next());			
		}
		return urlCompleta.toString();
	}

}
