package com.loow3.bazarapp.service;

import com.loow3.bazarapp.model.Client;
import com.loow3.bazarapp.repository.IClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
@Service
public class ClientService implements IClientService{
    @Autowired
    IClientRepository pR;

    @Override
    public Optional<Client> getClient(Long id) {
        return pR.findById(id);
    }

    @Override
    public List<Client> listClients() {
        return pR.findAll();
    }

    @Override
    public void createClient(Client client) {
        HashMap<String, Object> datos = new HashMap<String,Object>();
        pR.save(client);
        datos.put("status", "ok");
        datos.put("client", client);
        new ResponseEntity<>(datos, HttpStatus.CREATED);
    }

    @Override
    public void deleteClient(Long id) {
        pR.deleteById(id);
    }

    @Override
    public ResponseEntity<Object> updateClient(Long id, Client client) {
        HashMap<String, Object> datos = new HashMap<String, Object>();

        boolean clientExists = pR.existsById(id);
        if(!clientExists){
            datos.put("update", "fail");
            datos.put("cause", "client doesn't exits");
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.CONFLICT
            );
        }else{
            datos.put("update", "success");
            datos.put("client", client);
            this.createClient(client);
            return new ResponseEntity<>(
                    datos,
                    HttpStatus.OK
            );
        }
    }

}
