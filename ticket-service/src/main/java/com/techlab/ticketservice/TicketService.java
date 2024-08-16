package com.techlab.ticketservice;

import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.repositories.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    public Ticket findById(Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket save(Ticket ticket) {
        return ticketRepository.save(ticket);
    }

    public void delete(Integer id) {
        ticketRepository.deleteById(id);
    }

    public Ticket changeStatus(Integer id, String newStatus) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            ticket.setStatus(TicketStatus.valueOf(newStatus));
            return ticketRepository.save(ticket);
        }
        return null;
    }
}
