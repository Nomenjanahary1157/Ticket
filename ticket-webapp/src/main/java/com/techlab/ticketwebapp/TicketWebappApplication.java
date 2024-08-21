package com.techlab.ticketwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {
        "com.techlab.ticketservice",
        "com.techlab.ticketsecurity",
        "com.techlab.ticketwebapp",
        "com.techlab.ticketrepository"
})
@EnableJpaRepositories(basePackages = "com.techlab.ticketrepository.repositories")
@EntityScan(basePackages = "com.techlab.ticketrepository.models")
public class TicketWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketWebappApplication.class, args);
    }

}
