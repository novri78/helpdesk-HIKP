package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.repository.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findAll ();
    }

    public Ticket createTicket(Ticket ticket) {
        return ticketRepository.save (ticket);
    }
}
