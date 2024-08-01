package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDTO> createTicket(@RequestBody TicketDTO ticketDto) {
        TicketDTO createdTicket = ticketService.createTicket(ticketDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTicket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDTO> getTicketById(@PathVariable Long id) {
        try {
            TicketDTO ticketDTO = ticketService.getTicketById(id);
            return ResponseEntity.ok(ticketDTO);
        } catch (RuntimeException e) {
            log.error("Ticket not found for id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        try {
            TicketDTO updatedTicket = ticketService.updateTicket(id, ticketDTO);
            return ResponseEntity.ok(updatedTicket);
        } catch (Exception e) {
            log.error("Error updating ticket: ", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<List<TicketDTO>> getAllTickets() {
        List<TicketDTO> ticketDTOs = ticketService.getAllTickets ();
        return ResponseEntity.ok(ticketDTOs);
    }
}