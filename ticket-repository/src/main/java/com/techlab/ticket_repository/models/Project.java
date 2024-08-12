package com.techlab.ticket_repository.models;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Project {
    Integer id;
    Ticket ticket;
    User user;
}