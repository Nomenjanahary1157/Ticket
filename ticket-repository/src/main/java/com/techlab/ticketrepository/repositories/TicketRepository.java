package com.techlab.ticketrepository.repositories;

import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketrepository.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByStatus(TicketStatus status);
}
