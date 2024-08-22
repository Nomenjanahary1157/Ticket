package com.techlab.ticketrepository.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "clients")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client extends User {
    @Column(unique = true , nullable = false)
    private String trigram;
}

