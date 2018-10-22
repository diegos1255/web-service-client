package br.com.ws.service.client.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.ws.service.client.data.model.Client;

/**
 * Repositorio do Client
 * @author diego.oliveira
 *
 */
public interface ClientRepository extends JpaRepository<Client, Long>{

}
