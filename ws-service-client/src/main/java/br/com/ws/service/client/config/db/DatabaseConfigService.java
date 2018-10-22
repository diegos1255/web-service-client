package br.com.ws.service.client.config.db;

import org.springframework.stereotype.Service;

import br.com.ws.service.client.config.AbstractConfigurationService;

@Service
public class DatabaseConfigService extends AbstractConfigurationService<DatabaseConfigKey> {

	//private static final Logger logger = LogManager.getLogger(DatabaseConfigService.class);
	
	private static final String CONFIG_FILE_NAME = "db-config.properties";
	
	protected String getDefaultConfigValue(DatabaseConfigKey key) {
		switch (key) {
		case JDBC_DRIVER_NAME:
			return "com.mysql.jdbc.Driver";
		case JDBC_URL:
			return "jdbc:mysql://localhost:3306/wsServiceClient";
		case JDBC_USERNAME:
			return "root";
		case JDBC_PASSWORD:
			return "123456";
		case JDBC_SCHEMA:
			return "";
		case HIBERNATE_C3P0_ACQUIRE_INCREMENT:
			return "1";
		case HIBERNATE_C3P0_MAX_IDLE_TIME:
			return "120";
		case HIBERNATE_C3P0_MAX_SIZE:
			return "95";
		case HIBERNATE_C3P0_MAX_STATEMENTS:
			return "45";
		case HIBERNATE_C3P0_MIN_SIZE:
			return "10";
		case HIBERNATE_DIALECT:
			return "org.hibernate.dialect.MySQL5InnoDBDialect";
		case HIBERNATE_FORMAT_SQL:
			return "true";
		case HIBERNATE_HBM2DDL_AUTO:
			return "update";
		case HIBERNATE_SHOW_SQL:
			return "true";
		case HIBERNATE_USE_SQL_COMMENTS:
			return "false";
		case SPRING_JPA_DATABASE_NAME:
			return "MYSQL";
		default:
			break;
		}

		return null;
	}

	protected DatabaseConfigKey[] getAllConfigurationKeys() {
		return DatabaseConfigKey.values();
	}

	protected String getConfigFileName() {
		return CONFIG_FILE_NAME;
	}
	
}
