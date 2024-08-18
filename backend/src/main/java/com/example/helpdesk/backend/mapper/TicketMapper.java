package com.example.helpdesk.backend.mapper;

import com.example.helpdesk.backend.constant.TicketStatus;
import com.example.helpdesk.backend.dto.TicketDTO;
import com.example.helpdesk.backend.model.Ticket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {
    private static final Logger logger = LoggerFactory.getLogger(TicketMapper.class);

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
        ticketDTO.setPriorityStatus(ticket.getPriorityStatus());
        ticketDTO.setTicketStatus (TicketStatus.valueOf (ticket.getTicketStatus ().name ()));
        ticketDTO.setAssignTo (ticket.getAssignTo ( ));
        ticketDTO.setCreateDate (ticket.getCreateDate ());
        ticketDTO.setCloseDate (ticket.getCloseDate ());
        ticketDTO.setUserId (ticket.getUserId ());
        ticketDTO.setCategoryId (ticket.getCategory ());
        return ticketDTO;
    }

    // Convert TicketDTO to Ticket
    public Ticket toEntity(TicketDTO ticketDTO) {
        if (ticketDTO == null) {
            return null;
        }
        Ticket ticket = new Ticket (  );
        ticket.setId (ticketDTO.getId ( ));
        ticket.setTicketNo (ticketDTO.getTicketNo ());
        ticket.setTitle (ticketDTO.getTitle ());
        ticket.setDescription (ticketDTO.getDescription ( ));
        ticket.setPriorityStatus (ticketDTO.getPriorityStatus ());
        ticket.setTicketStatus (ticketDTO.getTicketStatus ());
        ticket.setAssignTo (ticketDTO.getAssignTo ());
        ticket.setCreateDate (ticketDTO.getCreateDate ());
        ticket.setCreateDate (ticketDTO.getCloseDate ());
        ticket.setUserId (ticketDTO.getUserId ( ));
        ticket.setCategory (ticketDTO.getCategoryId ( ));
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
            ticket.setAssignTo (ticketDTO.getAssignTo ());
            ticket.setCreateDate (ticketDTO.getCreateDate ());
            ticket.setCloseDate (ticketDTO.getCloseDate ());
            ticket.setUserId (ticketDTO.getUserId ( ));
            ticket.setCategory (ticketDTO.getCategoryId ( ));
        }
    }

}
