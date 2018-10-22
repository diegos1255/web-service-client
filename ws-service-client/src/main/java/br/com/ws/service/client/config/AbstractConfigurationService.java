package br.com.ws.service.client.config;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.builder.fluent.PropertiesBuilderParameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.configuration2.io.HomeDirectoryLocationStrategy;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class AbstractConfigurationService<T extends ConfigurationKey> {

	private static final Logger logger = LogManager.getLogger(AbstractConfigurationService.class);

	private static final String PROP_USER_HOME = "user.home";
	private static final String DEFAULT_CONFIG_REL_PATH = "/ws-service-client";
	private static final String CONFIG_FILE_COMMENT = " Ws Service Client";

	private FileBasedConfigurationBuilder<PropertiesConfiguration> builder;
	private PropertiesConfiguration config;

	private boolean readOnlyMode;

	protected AbstractConfigurationService() {
		initialize();
	}

	protected abstract String getDefaultConfigValue(T key);
	protected abstract T[] getAllConfigurationKeys();
	protected abstract String getConfigFileName();
	
	protected void initialize() {
		logger.info("Initializing configuration properties");

		PropertiesBuilderParameters params = new Parameters().properties().setEncoding("UTF-8")
				.setLocationStrategy(new HomeDirectoryLocationStrategy(getUserHome(), true))
				.setBasePath(DEFAULT_CONFIG_REL_PATH).setFileName(getConfigFileName());

		builder = new FileBasedConfigurationBuilder<PropertiesConfiguration>(PropertiesConfiguration.class)
				.configure(params);

		builder.setAutoSave(true);

		try {

			// try to load existing configuration
			config = builder.getConfiguration();

		} catch (ConfigurationException e1) {
			logger.info("Could not find config, creating default.");
			try {

				createInitialDefaultConfigFile();

				// try again
				config = builder.getConfiguration();

			} catch (ConfigurationException | IOException ex1) {
				logger.error("Cannot create local configuration file, loading default values in mem: " + ex1);
				readOnlyMode = true;
			}

		}
	}
	
	public String getString(T key) {
		if (readOnlyMode) {
			return getDefaultConfigValue(key);
		} else {
			String val = config.getString(key.getValue());
			if (val == null) {
				val = getDefaultConfigValue(key);
				config.setProperty(key.getValue(), val);
			}
			return val;
		}
	}

	public boolean getBoolean(T key) {
		if (readOnlyMode) {
			String val = getDefaultConfigValue(key);
			return Boolean.valueOf(val.toLowerCase());
		} else {
			String val = config.getString(key.getValue());
			if (val == null) {
				val = getDefaultConfigValue(key);
				config.setProperty(key.getValue(), val);
			}
			Boolean result = Boolean.valueOf(val.toLowerCase());
			return result;
		}
	}

	/**
	 * @param configFile
	 * @throws IOException
	 * @throws FileNotFoundException
	 */
	private void createInitialDefaultConfigFile() throws IOException, FileNotFoundException {
		File configFile = createConfigFile();
		if (configFile != null) {
			Properties props = loadDefaultConfiguration();
			FileOutputStream out = new FileOutputStream(configFile);
			props.store(out, CONFIG_FILE_COMMENT);
			out.close();
			logger.info("Config file saved at: " + configFile.getAbsolutePath());
		} else {
			throw new IOException("Could not create initial configuration.");
		}
	}

	private static String getUserHome() {
		return System.getProperty(PROP_USER_HOME);
	}

	private Properties loadDefaultConfiguration() {
		Properties configProperties = new Properties();

		T[] allKeys = getAllConfigurationKeys();
		for (T key : allKeys) {
			configProperties.put(key.getValue(), getDefaultConfigValue(key));
		}
		return configProperties;
	}

	private static String getConfigBasePath() {
		String userHomePath = getUserHome();
		String configFolder = userHomePath + "/" + DEFAULT_CONFIG_REL_PATH;
		return configFolder;
	}

	private static boolean createConfigBasePath() throws IOException {
		Path confPath = Paths.get(getConfigBasePath());
		if (!Files.exists(confPath)) {
			File cFolder = new File(confPath.toString());
			return cFolder.mkdir();
		}
		return true;
	}

	private File createConfigFile() throws IOException {
		if (createConfigBasePath()) {
			String configBasePath = getConfigBasePath();
			String filePath = configBasePath + "/" + getConfigFileName();
			logger.info("Creating file: " + filePath);
			File cFile = new File(filePath);
			boolean created = cFile.createNewFile();
			if (created) {
				return cFile;
			}
		}
		return null;
	}

}