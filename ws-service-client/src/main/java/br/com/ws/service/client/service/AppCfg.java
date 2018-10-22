package br.com.ws.service.client.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppCfg {

	private static Properties instance = null;

	public AppCfg() {

		if (instance == null) {

			String baseconfpath = System.getProperty("configsecurityproperties");
			InputStream is;
			try {

				is = new FileInputStream(baseconfpath + File.separator + "security.properties"); 
				instance = new Properties();
				instance.load(is);

			} catch (FileNotFoundException e) {

				e.printStackTrace();

			} catch (IOException e) {

				e.printStackTrace();

			}
		}
	}

	public String getUser() {
		return instance.getProperty("prop.user");
	}

	public String getPassword() {
		return instance.getProperty("prop.password");
	}

}
