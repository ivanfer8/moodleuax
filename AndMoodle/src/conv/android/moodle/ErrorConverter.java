package conv.android.moodle;

import com.thoughtworks.xstream.converters.Converter;
import com.thoughtworks.xstream.converters.MarshallingContext;
import com.thoughtworks.xstream.converters.UnmarshallingContext;
import com.thoughtworks.xstream.io.HierarchicalStreamReader;
import com.thoughtworks.xstream.io.HierarchicalStreamWriter;

import fac.android.moodle.ErrorException;

public class ErrorConverter implements Converter {
	public boolean canConvert(Class clazz) {
		return clazz.equals(ErrorConverter.class);
	}

	// This converts to XML
	public void marshal(Object value, HierarchicalStreamWriter writer,
			MarshallingContext context) {
	}

	// This converts from XML
	public Object unmarshal(HierarchicalStreamReader reader, UnmarshallingContext context) {
		ErrorException error = new ErrorException();
		System.out.println();
		try {

			reader.moveDown(); // we are in MESSAGE
			error.setDescError(reader.getValue().toString());
			error.setCodError("");
			return error;
		} catch (Exception e) {
			// Auto-generated catch block
			error.setDescError("ERROR: " + e.getMessage());
			return error;
		}
	}

	public void siguiente(HierarchicalStreamReader reader) {
		reader.moveUp();
		reader.moveUp();
		reader.moveDown();
		reader.moveDown();
	}

}
