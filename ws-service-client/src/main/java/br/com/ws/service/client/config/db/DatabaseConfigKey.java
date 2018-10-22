package br.com.ws.service.client.config.db;

import br.com.ws.service.client.config.ConfigurationKey;

public enum DatabaseConfigKey implements ConfigurationKey {

	JDBC_DRIVER_NAME("jdbc.driverClassName"),
	JDBC_URL("jdbc.url"),
	JDBC_SCHEMA("jdbc.schema"),
	JDBC_USERNAME("jdbc.username"),
	JDBC_PASSWORD("jdbc.password"),
	
	HIBERNATE_DIALECT("hibernate.dialect"),
	HIBERNATE_SHOW_SQL("hibernate.show_sql"),
	HIBERNATE_FORMAT_SQL("hibernate.format_sql"),
	HIBERNATE_USE_SQL_COMMENTS("hibernate.use_sql_comments"),
	HIBERNATE_HBM2DDL_AUTO("hibernate.hbm2ddl.auto"),
	
	HIBERNATE_C3P0_ACQUIRE_INCREMENT("hibernate.c3p0.acquireIncrement"),
	HIBERNATE_C3P0_MIN_SIZE("hibernate.c3p0.min_size"),
	HIBERNATE_C3P0_MAX_SIZE("hibernate.c3p0.max_size"),
	HIBERNATE_C3P0_MAX_IDLE_TIME("hibernate.c3p0.max_idletime"),
	HIBERNATE_C3P0_MAX_STATEMENTS("hibernate.c3p0.max_statements"),
	
	SPRING_JPA_DATABASE_NAME("jpa.database.name");

	private final String key;

	DatabaseConfigKey(String key) {
		this.key = key;
	}

	@Override
	public String toString() {
		return getValue();
	}

	public String getValue() {
		return key;
	}
}