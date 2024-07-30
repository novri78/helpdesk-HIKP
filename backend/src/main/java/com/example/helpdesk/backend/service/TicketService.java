package com.example.helpdesk.backend.service;

import com.example.helpdesk.backend.dto.ActivityDTO;
import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.ActivityLog;
import com.example.helpdesk.backend.model.Category;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.repository.TicketRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ActivityLogService activityLogService;

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

//    private Long generateTicketId(User user) {
//        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyy");
//        String datePart = sdf.format(new Date());
//        String departmentInitials = user.getDepartment().substring(0, 2).toUpperCase();
//
//        // Get the count of tickets created today
//        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//        long countToday = ticketRepository.countByCreationDate(today);
//        String dailySequence = String.format("%04d", countToday + 1); // 4 digits sequence number starting from 1
//
//        // Combine parts to create a unique ID
//        String customId = dailySequence + departmentInitials + datePart;
//        return Long.valueOf(customId.hashCode()); // Use hashcode to generate a unique long value
//    }

    private long generateTicketId() {
        // Get the highest ID currently in use and increment it
        Long maxId = ticketRepository.findMaxId();
        return (maxId == null ? 0 : maxId) + 1;
    }

    private ActivityDTO convertToActivityDTO(ActivityLog activityLog) {
        ActivityDTO activityDTO = new ActivityDTO ();
        activityDTO.setId (activityLog.getId ( ));
        activityDTO.setTimestamp (activityLog.getTimestamp ());
        activityDTO.setDescription (activityLog.getDescription ());
        activityDTO.setTicketStatus (activityLog.getTicketStatus ().getStatus ());
        activityDTO.setTicketId (activityLog.getTicketId ().getId ());
        return activityDTO;
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

    private void logActivity(Ticket ticket, String description) {
        ActivityDTO activityDTO = new ActivityDTO ();
        activityDTO.setTimestamp(new Date());
        activityDTO.setDescription(description);
        activityLogService.createActivityLog (activityDTO);
    }

    public TicketDTO createTicket(TicketDTO ticketDTO) {
        Ticket ticket = convertToEntity (ticketDTO);
        User user = userRepository.findById (ticketDTO.getUserId ())
                .orElseThrow (() -> new RuntimeException ("Ticket not found"));
        Category category = categoryRepository.findById (ticketDTO.getCategoryId ())
                        .orElseThrow (()-> new RuntimeException ("Ticket not found"));
        ticket.setUser (user);
        ticket.setCategory (category);
        ticket.setTitle (ticketDTO.getTitle ());
        ticket.setDescription (ticketDTO.getDescription ());
        ticket.setCreationDate(new Date());
        ticket.setClosureDate (new Date (  ));
        ticket.setId(generateTicketId()); // Set sequential ticketId ID
        ticket.setPriority (ticketDTO.getPriority ());
        ticketRepository.save (ticket);
        logActivity (ticket, "Ticket successfully created ");
        return convertToDTO (ticket);
    }

    public  TicketDTO updateTicket(Long id, TicketDTO ticketDTO) {
        Ticket ticket = ticketRepository.findById (id)
                .orElseThrow ( ()-> new RuntimeException("Ticket not found") );
        ticket.setDescription (ticketDTO.getDescription ());
        ticket.setPriority (ticketDTO.getPriority ());
        ticket.setStatus (ticketDTO.getStatus ());
        ticket.setClosureDate (ticketDTO.getClosureDate ());
        ticketRepository.save (ticket);
        logActivity (ticket, "Ticket successfully updated");
        return convertToDTO (ticket);
    }

    public void deleteTicketById(Long id) {
        Ticket ticket = ticketRepository.findById (id)
                        .orElseThrow ( () -> new RuntimeException("Ticket not found"));
        logActivity(ticket, "Ticket deleted"); // Log activity
        ticketRepository.deleteById (id);
    }
}
