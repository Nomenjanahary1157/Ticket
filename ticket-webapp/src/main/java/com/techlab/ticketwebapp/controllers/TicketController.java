package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.TicketRepository;
import com.techlab.ticketservice.services.TicketService;
import com.techlab.ticketservice.services.UserService;
import com.techlab.ticketwebapp.utils.TicketRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/api/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('CLI')")
    public ResponseEntity<?> save(@RequestBody Ticket ticket) {
        try {
            var savedticket = ticketService.save(ticket);
            userService.notifyAdminOnTicketCreate(savedticket);
            return ResponseEntity.ok(savedticket);
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

    @PutMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('CP','BA','DEV','CLI')")
    public ResponseEntity<?> updateStatus(@PathVariable Integer id, @RequestParam String status) {
        try {
            Ticket updatedTicket = ticketService.changeStatus(id, status);
            if (updatedTicket != null) {
                userService.notifyAdminOnTicketUpdate(updatedTicket);
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
    @PreAuthorize("hasAnyRole('CLI')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            ticketService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // TODO: handle exception
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

    @PutMapping("/affect")
    public ResponseEntity<?> addUserToTicket(@RequestParam Integer ticketId, @RequestParam Integer userId) {
        try {
            Ticket ticket = ticketService.findById(ticketId);
            User user = userService.findById(userId);
            if (ticket == null || user == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(userService.assignTicketToUser(userId,ticketId));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/tickets")
    public ResponseEntity<List<Ticket>> getUserTickets(@PathVariable Integer userId) {
        try {
            String sql = """
                    SELECT t.*
                    FROM tickets t
                    INNER JOIN user_tickets ut ON t.id = ut.ticket_id
                    INNER JOIN users u ON u.id = ut.user_id
                    WHERE u.id = ?
                    AND t.status IN ('DONE', 'TO_DO', 'IN_PROGRESS');
                    """;

            // Execute the query and map the results to a list of Ticket objects
            List<Ticket> tickets = jdbcTemplate.query(sql, new Object[]{userId}, new TicketRowMapper());

            return ResponseEntity.ok(tickets);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
