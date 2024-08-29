package com.techlab.ticketbatch;

import com.techlab.ticketrepository.models.Ticket;
import com.techlab.ticketservice.services.TicketService;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.transforms.Create;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;
import org.apache.beam.sdk.values.PCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
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

        // Generate a timestamp for the batch file
        String timestamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());

        // Create a single CSV file for all tickets
        ticketPCollection.apply("Format as CSV", ParDo.of(new ConvertToCsvFn()))
                .apply("Write Batch CSV to Single File", TextIO.write()
                        .to("output/tickets_batch_" + timestamp)
                        .withSuffix(".csv")
                        .withNumShards(1));

        // Apply transformations to write each ticket to its own CSV file
        ticketPCollection.apply("Write Each Ticket to CSV", ParDo.of(new WriteTicketToCsvFn()));

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

    private static class ConvertToCsvFn extends DoFn<TicketDTO, String> {
        @ProcessElement
        public void processElement(@Element TicketDTO ticketDTO, OutputReceiver<String> out) {
            out.output(String.join(",",
                    String.valueOf(ticketDTO.getId()),
                    ticketDTO.getName(),
                    ticketDTO.getDescription(),
                    ticketDTO.getTicketNumber(),
                    ticketDTO.getStatus(),
                    ticketDTO.getClientId() != null ? String.valueOf(ticketDTO.getClientId()) : ""
            ));
        }
    }

    private static class WriteTicketToCsvFn extends DoFn<TicketDTO, Void> {
        private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";

        @ProcessElement
        public void processElement(@Element TicketDTO ticketDTO) {
            String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
            String fileName = String.format("output/%s_%d.csv", timestamp, ticketDTO.getId());

            // Write each ticketDTO to its own file
            // This example uses standard Java IO, adapt as needed for Beam
            try (java.io.FileWriter fileWriter = new java.io.FileWriter(fileName)) {
                fileWriter.write(convertToCsv(ticketDTO));
            } catch (Exception e) {
                // Handle file write exception
                e.printStackTrace();
            }
        }

        private String convertToCsv(TicketDTO ticketDTO) {
            return String.join(",",
                    String.valueOf(ticketDTO.getId()),
                    ticketDTO.getName(),
                    ticketDTO.getDescription(),
                    ticketDTO.getTicketNumber(),
                    ticketDTO.getStatus(),
                    ticketDTO.getClientId() != null ? String.valueOf(ticketDTO.getClientId()) : ""
            );
        }
    }
}