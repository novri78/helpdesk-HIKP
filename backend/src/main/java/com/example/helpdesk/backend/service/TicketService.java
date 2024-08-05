package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.repository.ActivityLogRepository;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.repository.TicketRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import com.example.helpdesk.backend.util.TicketStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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

    private final SimpleDateFormat dateFormat = new SimpleDateFormat ( "ddMMyy" );

    private String generateTicketNo(Ticket ticket) {
        String date = dateFormat.format (new Date());
        String categoryName = ticket.getCategory ().getName ();
        String categoryInitial = categoryName.length () >= 3 ? categoryName.substring (0, 3).toUpperCase() : categoryName.toUpperCase ();
        int sequenceNumber = ticketRepository.countByCreationDate(new Date()) + 1;
        return String.format ("%03d/%s/%s", sequenceNumber, categoryInitial, date);
    }

    private TicketDTO mapToDTO(Ticket ticket) {
        if (ticket.getId () == null) {
            throw new IllegalArgumentException("Ticket ID must not be null");
        }
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId()); // Set the id
        ticketDTO.setTicketNo (ticket.getTicketNo ());
        ticketDTO.setTitle(ticket.getTitle());
        ticketDTO.setDescription(ticket.getDescription());
        ticketDTO.setPriorityStatus(ticket.getPriorityStatus ());
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
        ticket.setTicketNo (ticketDTO.getTicketNo ( ));
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriorityStatus (ticketDTO.getPriorityStatus ());
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
        activityLog.setTicketStatus (TicketStatus.fromValue (ticket.getTicketStatus ()));
        activityLog.setTicketId (activityLog.getTicketId ( ));
        activityLogRepository.save(activityLog);
    }

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList ());
    }

    @Transactional
    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = mapToEntity(ticketDTO);
        ticket.setTicketNo (generateTicketNo (ticket));
        Ticket savedTicket = ticketRepository.save(ticket);
        logActivity(savedTicket, "Ticket created");
        return mapToDTO(savedTicket);
    }

    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));
        return mapToDTO(ticket);
    }

    @Transactional
    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        Ticket existingTicket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));

        existingTicket.setTitle(ticketDTO.getTitle());
        existingTicket.setDescription(ticketDTO.getDescription());
        existingTicket.setPriorityStatus (ticketDTO.getPriorityStatus ());
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