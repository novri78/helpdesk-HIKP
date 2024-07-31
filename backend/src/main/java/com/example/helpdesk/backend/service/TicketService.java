package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.repository.ActivityLogRepository;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.repository.TicketRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ActivityLogRepository activityLogRepository;

    @Autowired
    private ActivityLogService activityLogService;

    private TicketDTO mapToDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setTitle(ticket.getTitle());
        ticketDTO.setDescription(ticket.getDescription());
        ticketDTO.setPriority(ticket.getPriority());
        ticketDTO.setStatus(ticket.getStatus ());
        ticketDTO.setCreatedBy(ticket.getCreatedBy());
        ticketDTO.setCreationDate(ticket.getCreationDate());
        ticketDTO.setClosureDate(ticket.getClosureDate());
        ticket.setUser(userRepository.findById(ticketDTO.getUserid ()).orElse (null));
        ticket.setCategory(categoryRepository.findById(ticketDTO.getCategoryId()).orElse(null));
        return ticketDTO;
    }

    private Ticket mapToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setStatus(ticketDTO.getStatus ());
        ticket.setCreatedBy(ticketDTO.getCreatedBy());
        ticket.setCreationDate(ticketDTO.getCreationDate());
        ticket.setClosureDate(ticketDTO.getClosureDate());
        ticket.setUser(userRepository.getById (ticketDTO.getUserid ()));
        ticket.setCategory(categoryRepository.getById (ticketDTO.getCategoryId()));
        return ticket;
    }

    private void logActivity(Ticket ticket, String description) {
        ActivityLog activityLog = new ActivityLog();
        activityLog.setTimestamp(new Date());
        activityLog.setDescription(description);
        activityLog.setTicketStatus(ticket.getStatus ());
        activityLog.setTicket (ticket);

        activityLogRepository.save(activityLog);
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        //Ticket ticket = new Ticket();
        Ticket ticket = mapToEntity(ticketDTO);
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setStatus(ticketDTO.getStatus ());
        ticket.setCreatedBy(ticketDTO.getCreatedBy());
        ticket.setCreationDate(ticketDTO.getCreationDate());
        ticket.setClosureDate(ticketDTO.getClosureDate());
        ticket.setUser(userRepository.getById (ticketDTO.getUserid ()));
        ticket.setCategory(categoryRepository.getById(ticketDTO.getCategoryId()));

        Ticket savedTicket = ticketRepository.save(ticket);
        logActivity(savedTicket, "Ticket created");

        return mapToDTO(savedTicket);
    }

    public TicketDTO getTicketById(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id :: " + id));
        return mapToDTO(ticket);
    }

    public TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        Optional<Ticket> optionalTicket = ticketRepository.findById(id);
        if (optionalTicket.isPresent()) {
            Ticket ticket = optionalTicket.get();
            ticket.setTitle(ticketDTO.getTitle());
            ticket.setDescription(ticketDTO.getDescription());
            ticket.setPriority(ticketDTO.getPriority());
            ticket.setStatus(ticketDTO.getStatus ());
            ticket.setCreatedBy(ticketDTO.getCreatedBy());
            ticket.setCreationDate(ticketDTO.getCreationDate());
            ticket.setClosureDate(ticketDTO.getClosureDate());
            ticket.setUser(userRepository.getById(ticketDTO.getUserid ()));
            ticket.setCategory(categoryRepository.getById(ticketDTO.getCategoryId()));

            Ticket updatedTicket = ticketRepository.save(ticket);
            logActivity(updatedTicket, "Ticket updated");
            return mapToDTO(updatedTicket);
        }
        return null;
    }

    public List<TicketDTO> getAllTickets() {
        List<Ticket> tickets = ticketRepository.findAll();
        return tickets.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

}
