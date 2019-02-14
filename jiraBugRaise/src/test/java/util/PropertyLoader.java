package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {

	
	private static Properties properties = null;
	public static String getProperty(String val) {
		if (properties == null) {
	        InputStream inputStream = PropertyLoader.class.getClassLoader()
	        		 .getResourceAsStream("config.properties");
	        properties = new Properties();
	        try {
				properties.load(inputStream);
				inputStream.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	    }
	    return properties.getProperty(val);
	}
}
