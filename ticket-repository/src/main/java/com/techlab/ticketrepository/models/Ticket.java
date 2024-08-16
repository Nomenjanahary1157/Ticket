package com.techlab.ticketrepository.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "tickets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private String ticketNumber;
    @Column(nullable = false)
    private String status;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @ManyToMany(mappedBy = "tickets")
    private Set<User> users;
}
