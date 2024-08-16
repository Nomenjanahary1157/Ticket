package com.techlab.ticketbatch;

import org.apache.beam.sdk.coders.DefaultCoder;
import org.apache.beam.sdk.coders.SerializableCoder;

import java.io.Serializable;

@DefaultCoder(SerializableCoder.class)
public class TicketDTO implements Serializable {
    private Integer id;
    private String name;
    private String description;
    private String ticketNumber;
    private String status;
    private String clientId;

    // Constructor
    public TicketDTO(Integer id, String name, String description, String ticketNumber, String status, Integer clientId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.ticketNumber = ticketNumber;
        this.status = status;
        this.clientId = Integer.toString(clientId);
    }

    // Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    @Override
    public String toString() {
        return "TicketDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", ticketNumber='" + ticketNumber + '\'' +
                ", status='" + status + '\'' +
                ", clientId='" + clientId + '\'' +
                '}';
    }
}
