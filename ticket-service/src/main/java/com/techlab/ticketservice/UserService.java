package com.techlab.ticketservice;

import com.techlab.ticketrepository.models.User;
import com.techlab.ticketrepository.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User save(User user) {
        return userRepository.save(user);
    }
}
