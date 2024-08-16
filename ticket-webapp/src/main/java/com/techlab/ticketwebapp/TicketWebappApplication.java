package com.techlab.ticketwebapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@ComponentScan(basePackages = {"com.techlab.ticketservice", "com.techlab.ticketsecurity", "com.techlab.ticketwebapp"})
@EnableJpaRepositories(basePackages = "com.techlab.ticketrepository.repositories")
@EntityScan(basePackages = "com.techlab.ticketrepository.models")
public class TicketWebappApplication {

    public static void main(String[] args) {
        SpringApplication.run(TicketWebappApplication.class, args);
    }

}
