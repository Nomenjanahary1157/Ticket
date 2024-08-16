package com.techlab.ticketservice;

import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }
    public User save(User user) {
        return userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public User findById(Integer id) {
        return userRepository.findById(id).orElse(null);
    }
}
