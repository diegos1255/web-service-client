package br.com.ws.service.client.init;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class WsServiceClientWebInitializer implements WebApplicationInitializer {

	private static final Logger logger = LogManager.getLogger(WsServiceClientWebInitializer.class);
	
	public static final String CONFIG_PACKAGE = "br.com.ws.service.client.init";
	public static final String SERVICE_DISPATCHER_NAME = "/s";
	public static final String SERVICE_DISPATCHER_MAPPING = SERVICE_DISPATCHER_NAME + "/*";
	
	public static String getApplicationVersion() {
		return WsServiceClientWebInitializer.class.getPackage().getImplementationVersion();
	}
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		
		logger.info("Initiate Spring context and scan for services annotated");
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        context.setConfigLocation(CONFIG_PACKAGE);
        servletContext.addListener(new ContextLoaderListener(context));
        
        logger.info("Dispatcher for all controller mappings");
        ServletRegistration.Dynamic dispatcher = servletContext.addServlet("DispatcherServlet", 
        		new DispatcherServlet(context));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping(SERVICE_DISPATCHER_MAPPING);
        
	}
}