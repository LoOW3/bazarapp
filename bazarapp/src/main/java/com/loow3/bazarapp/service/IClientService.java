package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Client;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface IClientService {
    public Optional<Client> getClient(Long id);
    public List<Client> listClients();
    public void createClient(Client client);
    public void deleteClient(Long id);
    public ResponseEntity<Object> updateClient(Long id, Map<String, Object> fields);

}
