package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketservice.services.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody Ticket ticket) {
        try {
            return ResponseEntity.ok(ticketService.save(ticket));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            Ticket ticket = ticketService.findById(id);
            if (ticket != null) {
                return ResponseEntity.ok(ticket);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        try {
            return ResponseEntity.ok(ticketService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            return ResponseEntity.ok(ticketService.save(ticket));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            Ticket updatedTicket = ticketService.changeStatus(id, status);
            if (updatedTicket != null) {
                return ResponseEntity.ok(updatedTicket);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            ticketService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
