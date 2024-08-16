package com.techlab.ticketservice;

import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket findTicketById(Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Ticket saveTicket(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(Integer id) {
        ticketRepository.deleteById(id);
    }

    public Ticket changeTicketStatus(Integer id, String newStatus) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            ticket.setStatus(newStatus);
            return ticketRepository.save(ticket);
        }
        return null;
    }
}
