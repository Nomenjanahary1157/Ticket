package com.techlab.ticketbatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = {"com.techlab.*"})
@EnableJpaRepositories(basePackages = "com.techlab.ticketrepository.repositories")
@EntityScan(basePackages = "com.techlab.ticketrepository.models")
public class TicketBatchApplication implements CommandLineRunner {

    private final BeamCsvExportService beamCsvExportService;

    public TicketBatchApplication(BeamCsvExportService beamCsvExportService) {
        this.beamCsvExportService = beamCsvExportService;
    }

    public static void main(String[] args) {
        SpringApplication.run(TicketBatchApplication.class, args);
    }

    @Override
    public void run(String... args) {
        beamCsvExportService.exportTicketsToCsv();
    }
}
