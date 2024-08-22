package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.dtos.ErrorResponseDTO;
import com.techlab.ticketrepository.enums.Role;
import com.techlab.ticketrepository.models.Client;
import com.techlab.ticketservice.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/api/clients")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody Client client) {
        try {
            client.setPassword(encoder.encode(client.getPassword()));
            client.setRole(Role.CLI);
            return ResponseEntity.ok(clientService.save(client));
        } catch (DataIntegrityViolationException e) {

            return ResponseEntity.badRequest().body(ErrorResponseDTO.builder().status(400).content("Username or trigram already exists").build());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body("Error creating user: " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            Client client = clientService.findById(id);
            if (client != null) {
                return ResponseEntity.ok(client);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(clientService.findAll());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody Client client) {
        try {
            return ResponseEntity.ok(clientService.save(client));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            clientService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }
}
