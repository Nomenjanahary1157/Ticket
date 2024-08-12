package main.java.com.techlab.ticket_repository.models;

@Entity
@Data 
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ticket {
    Integer idTicket;
    String name;
    String description;
    String ticketNumber;
    String statut;
}
