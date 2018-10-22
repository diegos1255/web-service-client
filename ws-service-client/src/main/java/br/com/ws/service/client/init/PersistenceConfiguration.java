package br.com.ws.service.client.init;

import java.beans.PropertyVetoException;
import java.util.Properties;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.AbstractEntityManagerFactoryBean;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import br.com.ws.service.client.config.db.DatabaseConfigKey;
import br.com.ws.service.client.config.db.DatabaseConfigService;

@Configuration
@EnableJpaRepositories(basePackages="br.com.ws.service.client")
@EnableTransactionManagement
public class PersistenceConfiguration {

	private static final Logger logger = LogManager.getLogger(PersistenceConfiguration.class);

	public static final String DOMAIN_ENTITY_SCAN = "br.com.ws.service.client.data.model";

	private String driverClassName;

	private String url;
	private String schema;
	private String username;
	private String password;
	
	private String hibernateDialect;
	private String hibernateShowSql;
	private String hibernateFormatSql;
	private String hibernateCommentSql;
	private String hibernateHbm2ddlAuto;
	
	private String hibernateC3P0AcquireIncrement;
	private String hibernateC3P0MinSize;
	private String hibernateC3P0MaxSize;
	private String hibernateC3P0MaxIdleTime;
	private String hibernateC3P0MaxStatements;

	private String databaseName;
	
	public PersistenceConfiguration(@Autowired DatabaseConfigService dbConfigService) {
		logger.debug("Initializing Persistence Configuration");
		initDatasourceProperties(dbConfigService);
	}

	public void initDatasourceProperties(DatabaseConfigService dbConfigService) {
		driverClassName = dbConfigService.getString(DatabaseConfigKey.JDBC_DRIVER_NAME);
		url = dbConfigService.getString(DatabaseConfigKey.JDBC_URL);
		schema = dbConfigService.getString(DatabaseConfigKey.JDBC_SCHEMA);
		username = dbConfigService.getString(DatabaseConfigKey.JDBC_USERNAME);
		password = dbConfigService.getString(DatabaseConfigKey.JDBC_PASSWORD);
		
		hibernateDialect = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_DIALECT);
		hibernateShowSql = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_SHOW_SQL);
		hibernateFormatSql = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_FORMAT_SQL);
		hibernateCommentSql = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_USE_SQL_COMMENTS);
		hibernateHbm2ddlAuto = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_HBM2DDL_AUTO);
		
		hibernateC3P0AcquireIncrement = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_C3P0_ACQUIRE_INCREMENT);
		hibernateC3P0MinSize = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_C3P0_MIN_SIZE);
		hibernateC3P0MaxSize = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_C3P0_MAX_SIZE);
		hibernateC3P0MaxIdleTime = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_C3P0_MAX_IDLE_TIME);
		hibernateC3P0MaxStatements = dbConfigService.getString(DatabaseConfigKey.HIBERNATE_C3P0_MAX_STATEMENTS);
	
		databaseName = dbConfigService.getString(DatabaseConfigKey.SPRING_JPA_DATABASE_NAME);
	}
	
	@Bean()
	public javax.sql.DataSource getDataSource() {

		ComboPooledDataSource ds = new ComboPooledDataSource();

		try {
			ds.setDriverClass(driverClassName);
			ds.setJdbcUrl(url);
			ds.setUser(username);
			ds.setPassword(password);

			ds.setAcquireIncrement(Integer.parseInt(hibernateC3P0AcquireIncrement));
			ds.setMinPoolSize(Integer.parseInt(hibernateC3P0MinSize));
			ds.setMaxPoolSize(Integer.parseInt(hibernateC3P0MaxSize));
			ds.setMaxIdleTime(Integer.parseInt(hibernateC3P0MaxIdleTime));
			ds.setMaxStatementsPerConnection(Integer.parseInt(hibernateC3P0MaxStatements));

		} catch (NumberFormatException | PropertyVetoException e) {
			logger.error("Could not create DataSource: " + e.getMessage());
		}

		return ds;
	}


	@Bean
	public Properties getHibernateProperties() {
		Properties properties = new Properties();
		properties.put("hibernate.dialect", hibernateDialect);
        properties.put("hibernate.show_sql", hibernateShowSql);
        properties.put("hibernate.format_sql", hibernateFormatSql);
        properties.put("hibernate.use_sql_comments", hibernateCommentSql);
        properties.put("hibernate.hbm2ddl.auto", hibernateHbm2ddlAuto);
        if(schema != null && !schema.isEmpty()){
        	properties.put("hibernate.default_schema", schema);	 
        }
		properties.put("sequence", "custom_hibernate_seq");
        
        return properties;
    }
    
    
    /**
     * Entity Manager Factory to Spring JPA with Hibernate Adapter
     * 
     * @param dataSource
     * @return
     */
    @Bean
    @Autowired
    public AbstractEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {

    	HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    	vendorAdapter.setDatabase(Database.valueOf(databaseName));
    	vendorAdapter.setGenerateDdl(true);
    	
    	LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    	factory.setJpaVendorAdapter(vendorAdapter);
    	factory.setPackagesToScan(DOMAIN_ENTITY_SCAN);
    	factory.setDataSource(dataSource);
    	factory.setJpaProperties(getHibernateProperties());
		
    	return factory;
    }    
    
    
    /**
     * Transaction Manager to Spring JPA
     * 
     * @param emf
     * @return
     */
    @Bean
    @Autowired
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
    	if(emf==null) return null;
    	JpaTransactionManager tm = new JpaTransactionManager();
    	tm.setEntityManagerFactory(emf);
    	return tm;
    }	
}