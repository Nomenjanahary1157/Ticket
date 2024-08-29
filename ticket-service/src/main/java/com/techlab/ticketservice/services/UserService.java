package com.techlab.ticketservice.services;

import com.techlab.ticketrepository.enums.Role;
import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Client;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.TicketRepository;
import com.techlab.ticketrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository ticketRepository;

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }

    public List<String> findAdminMail() {
        var cpList = userRepository.findByRole(Role.CP);
        var baList = userRepository.findByRole(Role.BA);
        cpList.addAll(baList);
        for (User user : cpList) {
            System.out.println("MAIL : " + user.getMail());
        }
        // Extract emails using streams and return the list of emails
        return cpList.stream().map(User::getMail).collect(Collectors.toList());
    }

    public void notifyAdminOnTicketCreate (Ticket ticket) {
        // Fetch the list of admin emails
        List<String> adminEmails = findAdminMail();
        // Create an EmailNotifier instance
        EmailNotifier notifier = new EmailNotifier(ticket, adminEmails);
        // Notify admins about the new ticket creation
        notifier.notifyTicketCreation();
    }

    public void notifyAdminOnTicketUpdate(Ticket ticket) {
        // Fetch the list of admin emails
        List<String> adminEmails = findAdminMail();

        // Create an EmailNotifier instance
        EmailNotifier notifier = new EmailNotifier(ticket, adminEmails);

        // Notify admins about the ticket update
        notifier.notifyTicketUpdate();
    }

    public void notifyDevOnAssign(User user , Ticket ticket){
        // Create an EmailNotifier instance
        EmailNotifier notifier = new EmailNotifier(ticket, Collections.singletonList(user.getMail()));
        notifier.notifyTicketAssignation();
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
            notifyDevOnAssign(user,ticket);
            // Persist the change in the database
            return userRepository.save(user);
        } else {
            throw new RuntimeException("User or Ticket not found.");
        }
    }
}
