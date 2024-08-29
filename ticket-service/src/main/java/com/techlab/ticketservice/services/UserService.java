package com.techlab.ticketservice.services;

import com.techlab.ticketrepository.enums.Role;
import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.TicketRepository;
import com.techlab.ticketrepository.repositories.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public Optional<User> findByName(String username) {
        return userRepository.findByUsername(username);
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public List<User> findDev() {
        return userRepository.findByRole(Role.DEV);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public User assignTicketToUser(Integer userId, Integer ticketId) {
        Optional<User> userOptional = userRepository.findById(userId);
        Optional<Ticket> ticketOptional = ticketRepository.findById(ticketId);

        if (userOptional.isPresent() && ticketOptional.isPresent()) {
            User user = userOptional.get();
            Ticket ticket = ticketOptional.get();

            // Assign the ticket to the user
            Set<Ticket> tickets = user.getTickets();
            tickets.add(ticket);
            user.setTickets(tickets);
            ticket.setStatus(TicketStatus.TO_DO);
            ticketRepository.save(ticket);
            // Persist the change in the database
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User or Ticket not found.");
        }
    }
}
