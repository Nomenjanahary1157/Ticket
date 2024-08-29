
package com.techlab.ticketbatch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication(scanBasePackages = {"com.techlab.*"})
@EnableScheduling
public class TicketBatchApplication {

    @Autowired
    public static void main(String[] args) {
        SpringApplication.run(TicketBatchApplication.class, args);
    }

}
