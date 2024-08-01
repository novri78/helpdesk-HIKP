package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.repository.ActivityLogRepository;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.repository.TicketRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ActivityLogRepository activityLogRepository;

    private TicketDTO mapToDTO(Ticket ticket) {
        if (ticket.getId () == null) {
            throw new IllegalArgumentException("Ticket ID must not be null");
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId()); // Set the id
        ticketDTO.setTitle(ticket.getTitle());
        ticketDTO.setDescription(ticket.getDescription());
        ticketDTO.setPriority(ticket.getPriority());
        ticketDTO.setTicketStatus(ticket.getTicketStatus ( ));
        ticketDTO.setCreatedBy(ticket.getCreatedBy());
        ticketDTO.setCreationDate(ticket.getCreationDate());
        ticketDTO.setClosureDate(ticket.getClosureDate());
        ticketDTO.setUserId(ticket.getUser().getId());
        ticketDTO.setCategoryId(ticket.getCategory().getId());
        return ticketDTO;

    }

    private Ticket mapToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId (ticketDTO.getId ());
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setTicketStatus(ticketDTO.getTicketStatus ( )); // Correctly map TicketStatus
        ticket.setCreatedBy(ticketDTO.getCreatedBy());
        ticket.setCreationDate(ticketDTO.getCreationDate());
        ticket.setClosureDate(ticketDTO.getClosureDate());
        ticket.setUser(userRepository.findById(ticketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for id: " + ticketDTO.getUserId())));
        ticket.setCategory(categoryRepository.findById(ticketDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found for id: " + ticketDTO.getCategoryId())));
        return ticket;
    }

    private void logActivity(Ticket ticket, String description) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setTimestamp(new Date());
        activityLog.setDescription(description);
        activityLog.setTicketStatus(ticket.getTicketStatus());
        activityLog.setTicket(ticket);
        activityLogRepository.save(activityLog);
    }

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList ());
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = mapToEntity(ticketDTO);
        Ticket savedTicket = ticketRepository.save(ticket);
        logActivity(savedTicket, "Ticket created");
        return mapToDTO(savedTicket);
    }

    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));
        return mapToDTO(ticket);
    }

    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));

        existingTicket.setTitle(ticketDTO.getTitle());
        existingTicket.setDescription(ticketDTO.getDescription());
        existingTicket.setPriority(ticketDTO.getPriority());
        existingTicket.setTicketStatus (ticketDTO.getTicketStatus ( ));
        existingTicket.setCreatedBy(ticketDTO.getCreatedBy());
        existingTicket.setCreationDate(ticketDTO.getCreationDate());
        existingTicket.setClosureDate(ticketDTO.getClosureDate());
        existingTicket.setUser(userRepository.findById(ticketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found for id: " + ticketDTO.getUserId())));
        existingTicket.setCategory(categoryRepository.findById(ticketDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found for id: " + ticketDTO.getCategoryId())));

        Ticket updatedTicket = ticketRepository.save(existingTicket);
        logActivity(updatedTicket, "Ticket updated");
        return mapToDTO(updatedTicket);
    }
}