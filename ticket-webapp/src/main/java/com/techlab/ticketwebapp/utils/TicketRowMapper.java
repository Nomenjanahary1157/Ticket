package com.techlab.ticketwebapp.utils;

import com.techlab.ticketrepository.enums.TicketStatus;
import com.techlab.ticketrepository.models.Ticket;
import org.springframework.jdbc.core.RowMapper;
import java.sql.ResultSet;
import java.sql.SQLException;

public class TicketRowMapper implements RowMapper<Ticket> {

    @Override
    public Ticket mapRow(ResultSet rs, int rowNum) throws SQLException {
        Ticket ticket = new Ticket();

        // Mapping the columns from the ResultSet to the Ticket object fields
        ticket.setId(rs.getInt("id"));
        ticket.setName(rs.getString("name"));
        ticket.setDescription(rs.getString("description"));
        ticket.setTicketNumber(rs.getString("ticket_number"));
        ticket.setStatus(TicketStatus.valueOf(rs.getString("status")));
        ticket.setCreatedAt(rs.getTimestamp("created_at"));
        ticket.setUpdatedAt(rs.getTimestamp("updated_at"));

        // If there are additional fields in the Ticket class, map them here
        // For example:
        // ticket.setSomeOtherField(rs.getType("column_name"));

        return ticket;
    }
}

