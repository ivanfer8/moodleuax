package conv.android.moodle;

import java.util.ArrayList;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fac.android.moodle.User;

public class MyUserConverter implements Converter {
	
	public boolean canConvert(Class clazz) {
		return clazz.equals(User.class);
	}

	// This converts to XML
	public void marshal(Object value, HierarchicalStreamWriter writer,MarshallingContext context) {
	}

	// This converts from XML
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {

		try {
			User user = null;
			ArrayList<User> listUser = new ArrayList<User>();
						
			reader.moveDown(); // we are in MULTIPLE
			while(reader.hasMoreChildren()){
				user = new User();
				reader.moveDown(); // SINGLE
				reader.moveDown(); // First KEY
				
				reader.moveDown(); // VALUE tag
				user.setId(reader.getValue().toString());
				siguiente(reader);
				//USERNAME // Second KEY. Have a look at the method hasMoreChildren()
				user.setName(reader.getValue());
				siguiente(reader);
				//FIRSTNAME
				user.setFirstName(reader.getValue());
				siguiente(reader);
				//LASTNAME
				user.setLastName(reader.getValue());
				siguiente(reader);
				//email
				user.setEmail(reader.getValue());
				siguiente(reader);
				//auth
				user.setAuth(reader.getValue());
				siguiente(reader);
				//confirmed
				user.setConfirmed(Boolean.parseBoolean(reader.getValue()));
				siguiente(reader);
				//idnumber
				user.setIdNumber(reader.getValue());
				siguiente(reader);
				//emailstop
				user.setEmailStop(Boolean.parseBoolean(reader.getValue()));
				siguiente(reader);
				//lang
				user.setLang(reader.getValue());
				siguiente(reader);
				//theme
				user.setTheme(reader.getValue());
				siguiente(reader);
				//timezone
				user.setTimeZone(reader.getValue());
				siguiente(reader);
				//mailformat
				user.setMailFormat(Boolean.parseBoolean(reader.getValue()));
				siguiente(reader);
				//description
				user.setDescription(reader.getValue());
				siguiente(reader);
				//city
				user.setCity(reader.getValue());
				siguiente(reader);
				//country
				user.setCountry(reader.getValue());
				siguiente(reader);
				//customfields
				user.setCustomFields(reader.getValue());
								
				reader.moveUp();
				reader.moveUp();
				reader.moveUp();
				listUser.add(user);
			}
			
			
			return listUser;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public void siguiente(HierarchicalStreamReader reader){
		reader.moveUp();
		reader.moveUp();
		reader.moveDown();
		reader.moveDown();
	}
}