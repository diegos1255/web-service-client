package br.com.ws.service.client.init;

import javax.servlet.ServletContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
@EnableWebMvc
public class WebMvcConfig extends WebMvcConfigurerAdapter {

	private static final Logger logger = LogManager.getLogger(WebMvcConfig.class);

	@Autowired
	private ServletContext servletContext;

	@Bean
	public Docket api() {
		
		logger.info("Creating Swagger Docket");
		
		// configuration
		boolean useAuto = true;
		logger.info("Swagger Docket, use auto path selector: " + useAuto);

		Docket docket;
		if(useAuto) {
			
			logger.info("Configuring Docket as context: " + servletContext.getContextPath());
			docket = new Docket(DocumentationType.SWAGGER_2)
					.select()
					.apis(RequestHandlerSelectors.any())
					.paths(PathSelectors.any())
					.build();
		} else {
			
			String host = "http://host";
			String appBasePath = "/basepath";

			logger.info("Configuring Docket host: " + host + ", basePath: " + appBasePath);
			
			docket = new Docket(DocumentationType.SWAGGER_2)
					.host(host)
					.pathProvider(new RelativePathProvider(servletContext) {
						@Override
						public String getApplicationBasePath() {
							return appBasePath;
						}
					});
		}

		return docket;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
		registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
	}
	
	@Bean
	public MultipartResolver multipartResolver(){
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
	    return multipartResolver;
	}
	
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
        return resolver;
	}
	
	@Bean
	public MappingJackson2HttpMessageConverter getHttpMessageConverter() {
		return new MappingJackson2HttpMessageConverter();
	}
	
}