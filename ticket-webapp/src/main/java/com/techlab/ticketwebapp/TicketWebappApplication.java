package com.techlab.ticketwebapp;

import com.techlab.ticketbatch.BeamCsvExportService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication(scanBasePackages = {
        "com.techlab.ticketservice",
        "com.techlab.ticketsecurity",
        "com.techlab.ticketwebapp",
        "com.techlab.ticketrepository",
        "com.techlab.ticketbatch"
})
@EnableJpaRepositories(basePackages = "com.techlab.ticketrepository.repositories")
@EntityScan(basePackages = "com.techlab.ticketrepository.models")
@EnableScheduling
public class TicketWebappApplication {

    private final BeamCsvExportService beamCsvExportService;

    @Autowired
    public TicketWebappApplication(BeamCsvExportService beamCsvExportService) {
        this.beamCsvExportService = beamCsvExportService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketWebappApplication.class, args);
    }

    @Scheduled(cron = "0 0 18 * * ?")
    public void scheduleExportTicketsToCsv() {
        beamCsvExportService.exportTicketsToCsv();
    }
}