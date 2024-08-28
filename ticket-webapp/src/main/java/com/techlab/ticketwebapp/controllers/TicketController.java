package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketservice.services.TicketService;
import com.techlab.ticketservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody Ticket ticket) {
        try {
            return ResponseEntity.ok(ticketService.save(ticket));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
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
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllTickets() {
        try {
            return ResponseEntity.ok(ticketService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @PostMapping
    public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
        try {
            return ResponseEntity.ok(ticketService.save(ticket));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
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
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            ticketService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/status")
    public ResponseEntity<List<Ticket>> getByStatus(@RequestParam(required = false) TicketStatus status) {
        if (status != null) {
            List<Ticket> tickets = ticketService.findByStatus(status);
            return (!tickets.isEmpty()) ? ResponseEntity.ok(tickets) : ResponseEntity.notFound().build();
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> addUserToTicket(@RequestParam Integer ticketId, @RequestParam Integer userId) {
        try {
            Ticket ticket = ticketService.findById(ticketId);
            User user = userService.findById(userId);
            if (ticket == null || user == null) {
                return ResponseEntity.notFound().build();
            }
            ticketService.addUserToTicket(ticket, user);
            ticket.setStatus(TicketStatus.valueOf("TO_DO"));
            ticketService.save(ticket);
            return ResponseEntity.ok(ticket);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

}
