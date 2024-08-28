package com.example.helpdesk.backend.mapper;

import com.example.helpdesk.backend.constant.PriorityStatus;
import com.example.helpdesk.backend.constant.Role;
import com.example.helpdesk.backend.constant.TicketStatus;
import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.Category;
import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.model.User;
import com.example.helpdesk.backend.repository.CategoryRepository;
import com.example.helpdesk.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class TicketMapper {
    private static final Logger logger = LoggerFactory.getLogger(TicketMapper.class);

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private final CategoryRepository categoryRepository;

    // Convert Ticket to TicketDTO
    public TicketDTO toDTO(Ticket ticket) {
        if(ticket == null) {
            return  null;
        }
        TicketDTO ticketDTO = new TicketDTO (  );
        ticketDTO.setId (ticket.getId ());
        ticketDTO.setTicketNo (ticket.getTicketNo ());
        ticketDTO.setTitle (ticket.getTitle ());
        ticketDTO.setDescription (ticket.getDescription ( ));
        ticketDTO.setPriorityStatus(ticket.getPriorityStatus ());
        ticketDTO.setTicketStatus (ticket.getTicketStatus ());
        ticketDTO.setAssignTo (ticket.getAssignTo ().getId ());
        ticketDTO.setCreateDate (ticket.getCreateDate ());
        ticketDTO.setCloseDate (ticket.getCloseDate ());
        ticketDTO.setUserId (ticket.getUser ().getId ());
        ticketDTO.setCategoryId (ticket.getCategory ( ).getId ( ));

        logger.info("TicketDTO details: {}", ticketDTO);

        return ticketDTO;
    }

    // Convert TicketDTO to Ticket
    public Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }

        Ticket ticket = new Ticket();
        ticket.setId(ticketDTO.getId());
        ticket.setTicketNo(ticketDTO.getTicketNo());
        ticket.setTitle (ticketDTO.getTitle ( ));
        ticket.setDescription(ticketDTO.getDescription());
        ticket.setPriorityStatus (ticketDTO.getPriorityStatus ());
        // Validate and set priority status
//        try {
//            ticket.setPriorityStatus(PriorityStatus.valueOf (ticketDTO.getPriorityStatus ().name ().toUpperCase ()));
//        } catch (IllegalArgumentException e) {
//            throw new RuntimeException("Invalid priority status: " + ticketDTO.getPriorityStatus());
//        }

        // Validate and set ticket status
        try {
            ticket.setTicketStatus(TicketStatus.valueOf(ticketDTO.getTicketStatus ().name ().toUpperCase ()));
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid ticket status: " + ticketDTO.getTicketStatus());
        }

        User supportUser = userRepository.findByIdAndRole(ticketDTO.getAssignTo(), Role.SUPPORT)
                .orElseThrow(() -> new RuntimeException("User with SUPPORT role not found"));
        ticket.setAssignTo(supportUser);

        ticket.setCreateDate(ticketDTO.getCreateDate());
        ticket.setCloseDate(ticketDTO.getCloseDate());

        User user = userRepository.findById(ticketDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));
        ticket.setUser(user);

        Category category = categoryRepository.findById(ticketDTO.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        ticket.setCategory(category);

        return ticket;
    }


    public void updateEntity(TicketDTO ticketDTO, Ticket ticket) {
        if (ticketDTO != null && ticket != null) {
            ticket.setId (ticketDTO.getId ( ));
            ticket.setTicketNo (ticketDTO.getTicketNo ( ));
            ticket.setTitle (ticketDTO.getTitle ( ));
            ticket.setDescription (ticket.getDescription ( ));
            ticket.setPriorityStatus (ticketDTO.getPriorityStatus ());
            ticket.setTicketStatus (ticketDTO.getTicketStatus ());
            ticket.setAssignTo (userRepository.getById (ticketDTO.getAssignTo ()));
            ticket.setCreateDate (ticketDTO.getCreateDate ());
            ticket.setCloseDate (ticketDTO.getCloseDate ());
            ticket.setUser (userRepository.getById (ticketDTO.getUserId ()));
            ticket.setCategory (categoryRepository.getById (ticketDTO.getCategoryId ()));

            logger.info("TicketDTO details: {}", ticketDTO);

        }
    }

}
