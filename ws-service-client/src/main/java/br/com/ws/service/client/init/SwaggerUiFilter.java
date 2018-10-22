package br.com.ws.service.client.init;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;


public class SwaggerUiFilter implements Filter {

	private String swaggerUiUri;
	private String swaggerUiUriNoService;
	
	public static final String SWAGGER_UI_HTML_FILE = "swagger-ui.html";
	
    /**
     * Default constructor. 
     */
    public SwaggerUiFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, 
			ServletResponse response, FilterChain chain) throws IOException, ServletException {
		if(request instanceof HttpServletRequest) {
			HttpServletRequest req = (HttpServletRequest) request;
			if(swaggerUiUri==null) {
				swaggerUiUri = req.getContextPath() + WsServiceClientWebInitializer.SERVICE_DISPATCHER_NAME
						+ "/" + SWAGGER_UI_HTML_FILE;
				swaggerUiUriNoService = req.getContextPath() + "/" + SWAGGER_UI_HTML_FILE;
			}
			if(req.getRequestURI().equals(swaggerUiUriNoService)) {
				String htmlCode = "<html>"
						+ "<meta http-equiv=\"refresh\" content=\"0\" url=\"" + swaggerUiUri + "\">"
						+ "</html>";
				System.out.println("C: " + htmlCode);
				response.getWriter().write(htmlCode);
				return;
			}
			
		}
		
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}