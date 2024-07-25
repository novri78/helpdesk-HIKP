package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll ();
    }

    public Ticket getTicketById(Long id) {
       Optional<Ticket> ticket =  ticketRepository.findById (id);
       if(ticket.isPresent ()) {
           return ticket.get ();
       } else {
           throw new RuntimeException("User not found for id :: " + id);
       }
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save (ticket);
    }

    public void deleteTicketById(Long id) {
        ticketRepository.deleteById (id);
    }
}
