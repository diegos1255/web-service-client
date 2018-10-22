package br.com.ws.service.client.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import br.com.ws.service.client.service.AppCfg;

@Service
public class WsServiceClientHelper {

	private static final Logger logger = LogManager.getLogger(WsServiceClientHelper.class);
	
	private AppCfg appCfg = new AppCfg();
	
	/**
	 * Metodo responsavel por validar o usuario de acesso ao ws
	 * @param user
	 * @param password
	 * @return
	 */
	public Boolean isAuthorized(String user, String password) {
		
		String METHOD_NAME = "isAuthorized";
		
		logger.debug("Inicio do metodo: " + METHOD_NAME);
		
		
		if (!this.appCfg.getUser().equals(user) && !this.appCfg.getPassword().equals(password)) {
			
			logger.debug("Usuario invalido: User = " + user + "Password = " + password);
			
			return Boolean.FALSE;
			
		}
		
		logger.debug("Fim do metodo: " + METHOD_NAME);
		
		return Boolean.TRUE;
		
	}

	/**
	 * @return the appCfg
	 */
	public AppCfg getAppCfg() {
		return appCfg;
	}

	/**
	 * @param appCfg the appCfg to set
	 */
	public void setAppCfg(AppCfg appCfg) {
		this.appCfg = appCfg;
	}
	
}