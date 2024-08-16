package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.models.User;
import com.techlab.ticketservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody User user) {
        return ResponseEntity.ok(userService.save(user));
    }
}
