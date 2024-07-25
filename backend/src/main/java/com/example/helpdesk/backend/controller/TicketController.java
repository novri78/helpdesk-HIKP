package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<Ticket> getAllTickets() {
        return ticketService.getAllTickets ();
    }

    @GetMapping("{id}")
    public Ticket getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById (id);
    }

    @PostMapping
    public Ticket createTicket(@RequestBody Ticket ticket) {
        return ticketService.createTicket (ticket);
    }

    @DeleteMapping
    public void deleteTicketById(@PathVariable Long id) {
        ticketService.deleteTicketById (id);
    }
}
