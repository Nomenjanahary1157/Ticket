package com.techlab.ticketbatch;

import org.apache.beam.sdk.transforms.DoFn;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = "com.techlab.ticketservice")
public class ConvertToCsvFn extends DoFn<TicketDTO, String> {

    @ProcessElement
    public void processElement(@Element TicketDTO ticketDTO, OutputReceiver<String> out) {
        String csvLine = String.join(",",
                String.valueOf(ticketDTO.getId()),          // ID
                ticketDTO.getName(),                        // Name
                ticketDTO.getDescription(),                 // Description
                ticketDTO.getTicketNumber(),                // Ticket Number
                ticketDTO.getStatus(),                      // Status
                ticketDTO.getClientId()                     // Client ID
        );
        out.output(csvLine);
    }
}
