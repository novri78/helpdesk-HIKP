package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.TicketRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    private TicketDTO convertToDTO(Ticket ticket) {
        TicketDTO ticketDTO = new TicketDTO();
        ticketDTO.setId(ticket.getId());
        ticketDTO.setTitle(ticket.getTitle());
        ticketDTO.setDescription(ticket.getDescription());
        ticketDTO.setPriority(ticket.getPriority());
        ticketDTO.setStatus(ticket.getStatus());
        ticketDTO.setCreationDate(ticket.getCreationDate());
        ticketDTO.setClosureDate(ticket.getClosureDate());
        ticketDTO.setUserId(ticket.getUser().getId());
        ticketDTO.setCategoryId(ticket.getCategory().getId());
        return ticketDTO;
    }

    private Ticket convertToEntity(TicketDTO ticketDTO) {
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketDTO.getTitle());
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriority(ticketDTO.getPriority());
        ticket.setStatus(ticketDTO.getStatus());
        ticket.setCreationDate(ticketDTO.getCreationDate());
        ticket.setClosureDate(ticketDTO.getClosureDate());
        return ticket;
    }

    private Long generateTicketId(User user) {
        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
        String datePart = sdf.format(new Date());
        String departmentInitials = user.getDepartment ().substring (0,1).toUpperCase (  );

        // Get the count of tickets created today
        String today = new SimpleDateFormat ( "yyyy-MM-dd" ).format (new Date (  ));
        long countToday = ticketRepository.countByCreationDate(today);
        String dailySequence = String.format ("%04d", countToday + 1); // 4 digits sequence number

        return Long.valueOf (dailySequence + departmentInitials + datePart); //concate for generateTicketId
    }

    public List<TicketDTO> getAllTickets() {
        return ticketRepository.findAll ().stream ()
                .map (this::convertToDTO)
                .collect(Collectors.toList());
    }

    public TicketDTO getTicketById(Long id) {
       Ticket ticket =  ticketRepository.findById (id)
               .orElseThrow ( () -> new RuntimeException ("Ticket not found"));
       return convertToDTO (ticket);
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity (ticketDTO);
        User user = userRepository.findById (ticketDTO.getUserId ())
                .orElseThrow (() -> new RuntimeException ("Ticket not found"));
        ticket.setUser (user);
        ticket.setId (generateTicketId (user)); // Set custom ticket ID
        ticketRepository.save (ticket);
        return convertToDTO (ticket);
    }

    public void deleteTicketById(Long id) {
        ticketRepository.deleteById (id);
    }
}
