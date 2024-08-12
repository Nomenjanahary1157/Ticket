package main.java.com.techlab.ticket_repository.models;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {
    Integer idUser;
    String usermame;
    String password;
    String roles;
}
