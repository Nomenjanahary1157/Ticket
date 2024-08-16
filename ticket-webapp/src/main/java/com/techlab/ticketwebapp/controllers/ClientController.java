package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.models.Client;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketservice.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody Client client) {
        return ResponseEntity.ok(clientService.save(client));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Client> getById(@PathVariable Integer id) {
        Client client = clientService.findById(id);
        if (client != null) {
            return ResponseEntity.ok(client);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Client> getAll() {
        return clientService.findAll();
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        return clientService.save(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
