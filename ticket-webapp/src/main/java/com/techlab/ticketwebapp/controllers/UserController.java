package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.models.User;
import com.techlab.ticketsecurity.utils.JwtUtils;
import com.techlab.ticketservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder encoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/create")
    public ResponseEntity<?> save(@RequestBody User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        return ResponseEntity.ok(userService.save(user));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("ETAPE 1 : " + user.getUsername());
        var connectedUser = userService.findByName(user.getName()).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println("ETAPE 2");
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getName(), user.getPassword()));
        System.out.println("ETAPE 3");
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        System.out.println("PASSWORD 2 : " + userDetails.getPassword());
        System.out.println("PASSWORD 1 : " + connectedUser.getPassword());
        if (encoder.matches(user.getPassword(), userDetails.getPassword())) {
            response.put("content", jwtUtils.generateToken(userDetails));
            response.put("user", userDetails);
            return ResponseEntity.ok(response);

        } else return ResponseEntity.badRequest().body("Incorrect password");
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        User user = userService.findById(id);
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public List<User> getAll() {
        return userService.findAll();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
