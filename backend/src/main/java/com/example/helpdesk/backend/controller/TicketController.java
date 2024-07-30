package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.TicketDTO;
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
    public List<TicketDTO> getAllTickets() {
        return ticketService.getAllTickets ();
    }

    @GetMapping("{id}")
    public TicketDTO getTicketById(@PathVariable Long id) {
        return ticketService.getTicketById (id);
    }

    @PostMapping
    public TicketDTO createTicket(@RequestBody TicketDTO ticketDTO) {
        System.out.println("Received ticketId :" + ticketDTO);
        return ticketService.createTicket (ticketDTO);
    }

    @PutMapping("{id}")
    public  TicketDTO updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        return ticketService.updateTicket (id, ticketDTO);
    }

    @DeleteMapping
    public void deleteTicketById(@PathVariable Long id) {
        ticketService.deleteTicketById (id);
    }
}
