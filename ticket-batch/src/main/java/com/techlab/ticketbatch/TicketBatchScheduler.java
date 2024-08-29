
package com.techlab.ticketbatch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TicketBatchScheduler {

    private final BeamCsvExportService beamCsvExportService;

    public TicketBatchScheduler(BeamCsvExportService beamCsvExportService) {
        this.beamCsvExportService = beamCsvExportService;
    }

    @Scheduled(cron = "0 33 10 * * ?")
    public void scheduleExportTicketsToCsv() {
        beamCsvExportService.exportTicketsToCsv();
    }
}
