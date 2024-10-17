package com.loow3.bazarapp.controller;

import com.loow3.bazarapp.model.Client;
import com.loow3.bazarapp.service.IClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class ClientController {
    @Autowired
    IClientService pS;

    @PostMapping("/clientes/crear")
    public ResponseEntity<String> createClient(@RequestBody Client client){
        pS.createClient(client);
        return ResponseEntity.ok("successfully created");
    }

    @GetMapping("/clientes")
    public ResponseEntity<List<Client>> listClients(){
        return ResponseEntity.ok(pS.listClients());
    }

    @GetMapping("/clientes/{id}")
    public ResponseEntity<Optional<Client>> getClient(@PathVariable Long id){
        return ResponseEntity.ok(pS.getClient(id));
    }

    @DeleteMapping("/clientes/eliminar/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id){
        pS.deleteClient(id);
        return ResponseEntity.ok("successfully deleted");
    }

    @PutMapping("/clientes/editar/{id}")
    public ResponseEntity<Object> editClient(@PathVariable Long id,
                                              @RequestBody Client client){
        return pS.updateClient(id, client);
    }
}
