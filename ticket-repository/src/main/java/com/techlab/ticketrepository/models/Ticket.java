package com.techlab.ticketrepository.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.techlab.ticketrepository.enums.TicketStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tickets")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;
    @Column(nullable = false)
    private String ticketNumber;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.NOT_ASSIGNED;
    @CreationTimestamp
    private Timestamp createdAt;
    @UpdateTimestamp
    private Timestamp updatedAt;
    @JsonIgnore
    @ManyToMany(mappedBy = "tickets")
    private Set<User> users = new HashSet<>();

    @PostLoad
    @PostPersist
    @PostUpdate
    public void updateTicketNumber() {
        if (client != null && id != null) {
            this.ticketNumber = client.getTrigram() + "-" + this.id;
        }
    }
}
