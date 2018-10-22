package br.com.ws.service.client.controller;

import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ws.service.client.data.model.Client;
import br.com.ws.service.client.helper.WsServiceClientHelper;
import br.com.ws.service.client.service.ClientService;

/**
 * Controller do Client
 * @author diego.oliveira
 *
 */
@RestController
public class ClientController {

	private static final Logger logger = LogManager.getLogger(ClientController.class);

	@Autowired
	private ClientService clientService;
	
	@Autowired
	private WsServiceClientHelper wsServiceClientHelper;
	
	/**
	 * Metodo responsavel por salvar o client
	 * @param user
	 * @param password
	 * @param name
	 * @return
	 */
	@PostMapping(value = "/saveClient", produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> saveClient(@RequestParam("user") String user, @RequestParam("password") String password, @RequestParam("name") String name) {
		
		String METHOD_NAME = "saveClient";
		
		if (!wsServiceClientHelper.isAuthorized(user, password)) {
			
			logger.debug("Usuario nao autorizado. Usuario: " + user + ". Senha: " + password);
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
			
		}
		
		String uuid = null;
		uuid = UUID.randomUUID().toString();

		try {
			
			logger.debug("Iniciando metodo: " + METHOD_NAME);
			
			Client client = new Client();
			client.setId(uuid);
			client.setNome(name);
			
			clientService.save(client);

			logger.debug("Fim do metodo: " + METHOD_NAME);
			
		} catch (Exception e) {
			
			logger.debug("Erro ao salvar client: " + e.getMessage());
			
		}
		
		return ResponseEntity.ok(uuid);
		
	}
	
}
