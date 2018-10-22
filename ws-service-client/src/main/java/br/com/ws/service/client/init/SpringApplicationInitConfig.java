package br.com.ws.service.client.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;

import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "br.com.ws.service.client")
public class SpringApplicationInitConfig implements ApplicationListener<ApplicationEvent> {

	private static final Logger logger = LogManager.getLogger(SpringApplicationInitConfig.class);
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// Bean Manager Started
		if(event instanceof ContextRefreshedEvent) {
			
			ApplicationContext context = ((ContextRefreshedEvent) event).getApplicationContext();
		}
		
		// Shutting down application
		if(event instanceof ContextClosedEvent) {
			logger.debug("Spring context closed.");
		}
		
	}

}