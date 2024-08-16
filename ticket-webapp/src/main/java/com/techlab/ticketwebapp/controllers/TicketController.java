package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketservice.TicketService;
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
        return ResponseEntity.ok(ticketService.save(ticket));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ticket> getById(@PathVariable Integer id) {
        Ticket ticket = ticketService.findById(id);
        if (ticket != null) {
            return ResponseEntity.ok(ticket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.findAll();
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.save(ticket);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Ticket> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        Ticket updatedTicket = ticketService.changeStatus(id, status);
        if (updatedTicket != null) {
            return ResponseEntity.ok(updatedTicket);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        ticketService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
