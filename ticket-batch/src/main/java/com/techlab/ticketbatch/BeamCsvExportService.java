package com.techlab.ticketbatch;

import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketservice.services.TicketService;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@ComponentScan(basePackages = {"com.techlab.ticketservice","com.techlab.ticketrepository.model"})
public class BeamCsvExportService {

    @Autowired
    private TicketService ticketService;

    public void exportTicketsToCsv() {
        // Create the pipeline
        Pipeline pipeline = Pipeline.create();

        // Fetch tickets from TicketService
        List<Ticket> tickets = ticketService.findAll();

        // Convert Tickets to TicketDTO
        List<TicketDTO> ticketDTOs = tickets.stream()
                .map(this::convertToTicketDTO)
                .collect(Collectors.toList());

        // Create a PCollection from the list of TicketDTOs
        PCollection<TicketDTO> ticketPCollection = pipeline.apply(Create.of(ticketDTOs));

        // Apply transformations and write to CSV
        ticketPCollection.apply("Format as CSV", ParDo.of(new ConvertToCsvFn()))
                .apply("Write to CSV", TextIO.write()
                        .to("output/tickets")
                        .withSuffix(".csv")
                        .withNumShards(1));

        // Run the pipeline
        pipeline.run().waitUntilFinish();
    }

    private TicketDTO convertToTicketDTO(Ticket ticket) {
        return new TicketDTO(
                ticket.getId(),
                ticket.getName(),
                ticket.getDescription(),
                ticket.getTicketNumber(),
                ticket.getStatus().name(),
                ticket.getClient() != null ? ticket.getClient().getId() : null
        );
    }
}
