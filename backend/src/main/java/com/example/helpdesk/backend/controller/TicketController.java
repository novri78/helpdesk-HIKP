package com.example.helpdesk.backend.controller;

import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<?> createTicket(@Valid @RequestBody TicketDTO ticketDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList ());
            return ResponseEntity.badRequest().body(errorMessages);
        }
        // Proceed with creating the ticket
        TicketDTO createdTicket = ticketService.createNewTicket (ticketDTO);
        return ResponseEntity.ok (createdTicket);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTicketById(@PathVariable Long id) {
        try {
            TicketDTO ticketDTO = ticketService.getTicketById(id);
            return ResponseEntity.ok(ticketDTO);
        } catch (RuntimeException e) {
            log.error("Ticket not found for id: " + id, e);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Ticket not found with id: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDTO> updateTicket(@PathVariable Long id, @RequestBody TicketDTO ticketDTO) {
        try {
            TicketDTO updatedTicket = ticketService.updateTicketById (id, ticketDTO);
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