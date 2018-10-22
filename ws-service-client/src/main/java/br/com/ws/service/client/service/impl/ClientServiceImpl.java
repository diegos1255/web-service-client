package br.com.ws.service.client.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.ws.service.client.data.model.Client;
import br.com.ws.service.client.data.repository.ClientRepository;
import br.com.ws.service.client.service.ClientService;

@Service
public class ClientServiceImpl implements ClientService {

	@Autowired
	private ClientRepository clientRepository;
	
	@Override
	public Client save(Client client) {
		
		return clientRepository.save(client);
	}

}
