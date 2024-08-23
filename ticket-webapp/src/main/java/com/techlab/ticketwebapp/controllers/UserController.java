package com.techlab.ticketwebapp.controllers;

import com.techlab.ticketrepository.dtos.AuthResponseDTO;
import com.techlab.ticketrepository.dtos.ErrorResponseDTO;
import com.techlab.ticketrepository.enums.Role;
import com.techlab.ticketrepository.models.User;
import com.techlab.ticketsecurity.utils.JwtUtils;
import com.techlab.ticketservice.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
@RequestMapping("/api/users")
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
    @PreAuthorize("hasAnyRole('CP')")
    public ResponseEntity<?> save(@RequestBody User user) {
        try {
            user.setPassword(encoder.encode(user.getPassword()));
            return ResponseEntity.ok(userService.save(user));
        } catch (DataIntegrityViolationException e){
            return ResponseEntity.badRequest().body(ErrorResponseDTO
                    .builder()
                            .status(400)
                            .content("Username already exists")
                    .build());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body("Error creating user: " + e.getMessage());
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody User user) {
        try {
            //verify if user exists
            var connectedUser = userService.findByName(user.getUsername()).orElseThrow(() -> new UsernameNotFoundException("User not found"));

            //check if user has correct password
            if (!encoder.matches(user.getPassword(), connectedUser.getPassword())) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponseDTO.builder().user(null).content("Incorrect password").build());
            }

            //authenticate user
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

            return ResponseEntity.ok().body(AuthResponseDTO.builder().user(connectedUser).content(jwtUtils.generateToken((UserDetails) authentication.getPrincipal())).build());
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(AuthResponseDTO.builder().user(null).content("Bad credentials").build());
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body("An error has occurred : " + e.getMessage());
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('CP')")
    public ResponseEntity<?> getById(@PathVariable Integer id) {
        try {
            User user = userService.findById(id);
            if (user != null) {
                return ResponseEntity.ok(user);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('CP')")
    public ResponseEntity<?> getAll() {
        try {
            return ResponseEntity.ok(userService.findAll());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
            // TODO: handle exception
        }
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('CP')")
    public ResponseEntity<?> update(@RequestBody User user) {
        try {
            return ResponseEntity.ok(userService.save(user));
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('CP')")
    public ResponseEntity<?> delete(@PathVariable Integer id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            // TODO: handle exception
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }

    @GetMapping("/devUser")
    @PreAuthorize("hasAnyRole('CP')")
    public  ResponseEntity<?> getDevUser() {
        try {
            return  ResponseEntity.ok(userService.findDev());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(e.getMessage());
        }
    }
}
