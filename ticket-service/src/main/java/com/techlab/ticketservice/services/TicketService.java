package com.techlab.ticketservice.services;// TicketService.java

import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.TicketRepository;
import com.techlab.ticketrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    public Ticket findById(Integer id) {
        return ticketRepository.findById(id).orElse(null);
    }

    public List<Ticket> findAll() {
        return ticketRepository.findAll();
    }

    public Ticket save(Ticket ticket) {
        if (ticket.getStatus() == null) {
            ticket.setStatus(TicketStatus.NOT_ASSIGNED);
        }
        return ticketRepository.save(ticket);
    }

    public void delete(Integer id) {
        ticketRepository.deleteById(id);
    }

    public Ticket changeStatus(Integer id, String newStatus) {
        Ticket ticket = ticketRepository.findById(id).orElse(null);
        if (ticket != null) {
            ticket.setStatus(TicketStatus.valueOf(newStatus));
            if(newStatus.equals(String.valueOf(TicketStatus.RESOLVED))){
                notifyClientOnResolved(ticket);
            }
            return ticketRepository.save(ticket);
        }
        return null;
    }

    public List<Ticket> findByStatus(TicketStatus status) {
        return ticketRepository.findByStatus(status);
    }

    public void notifyClientOnResolved(Ticket ticket){
        // Create an EmailNotifier instance
        EmailNotifier notifier = new EmailNotifier(ticket, Collections.singletonList(ticket.getClient().getMail()));
        notifier.notifyTicketOnResolved();
    }
}
