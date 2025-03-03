package util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoadParameter {

	private static Properties properties;
	
	
	 static {
		properties = new Properties();
		
		try(InputStream is = new FileInputStream("file.config")){
			properties.load(is);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static String getValor(String chave) {
		return properties.getProperty(chave);
	}
	
	
}
