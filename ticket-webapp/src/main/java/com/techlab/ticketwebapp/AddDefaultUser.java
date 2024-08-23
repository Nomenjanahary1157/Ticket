package com.techlab.ticketwebapp;

import com.techlab.ticketrepository.enums.Role;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
@Component
public class AddDefaultUser implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder encoder;
    @Override
    public void run(String... args) throws Exception {
        try{
            User user = new User();
            user.setUsername("admin");
            user.setPassword(encoder.encode("admin"));
            user.setRole(Role.CP);
            user.setMail("raly.enterprise.mada@gmail.com");
            user.setTickets(new HashSet<>());
            userRepository.save(user);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Default user is already here!!");
        } catch (Exception e) {
            System.out.println("We got error on creating default user : " + e.getMessage());
        }
    }
}
