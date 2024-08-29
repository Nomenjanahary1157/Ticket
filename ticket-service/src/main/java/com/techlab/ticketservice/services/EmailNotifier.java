package com.techlab.ticketservice.services;

import com.techlab.ticketrepository.models.Ticket;
import jakarta.mail.*;
import jakarta.mail.internet.AddressException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EmailNotifier {

    private final String username;
    private final String password;
    private final Properties properties;
    private final Ticket ticket;
    private List<String> mailsReceptor;

    public EmailNotifier(Ticket ticket, List<String> mailsReceptor) {
        this.username = "raly.enterprise.mada@gmail.com";
        this.password = "fqii iacw ovao kyga";
        this.ticket = ticket;
        this.mailsReceptor = mailsReceptor;

        // Setup mail server properties
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
    }

    public void sendEmail(String email, String subject, String body) throws AddressException {
        // Validate email address
        if (email == null || !email.contains("@")) {
            throw new AddressException("Invalid email address: " + email);
        }

        // Create a mail session with authentication
        Session session = Session.getInstance(properties, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Create a MimeMessage object
            MimeMessage message = new MimeMessage(session);

            // Set the sender's email address
            message.setFrom(new InternetAddress(username));

            // Set the recipient's email address
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));

            // Set the email subject
            message.setSubject(subject);

            // Set the email body
            message.setText(body);

            // Send the email
            Transport.send(message);
            System.out.println("Email sent successfully to " + email);

        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    // Method to notify ticket creation
    public void notifyTicketCreation() {
        String subject = "New Ticket Created: ID " + this.ticket.getId();
        String body = "A new ticket with ID " + this.ticket.getId() + " with title as " + this.ticket.getName() + " is Created.";

        // Create a thread pool to send emails in parallel
        ExecutorService executor = Executors.newFixedThreadPool(mailsReceptor.size());

        for (String email : mailsReceptor) {
            executor.submit(() -> {
                try {
                    sendEmail(email, subject, body);
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor after tasks are completed
        executor.shutdown();
    }

    // Method to notify ticket update
    public void notifyTicketUpdate() {
        String subject = "Ticket Updated: ID " + this.ticket.getId();
        String body = "The ticket with ID " + this.ticket.getId() + " has been updated to status: " + this.ticket.getStatus() + ".";

        // Create a thread pool to send emails in parallel
        ExecutorService executor = Executors.newFixedThreadPool(mailsReceptor.size());

        for (String email : mailsReceptor) {
            executor.submit(() -> {
                try {
                    sendEmail(email, subject, body);
                } catch (AddressException e) {
                    e.printStackTrace();
                }
            });
        }

        // Shutdown the executor after tasks are completed
        executor.shutdown();
    }
}
