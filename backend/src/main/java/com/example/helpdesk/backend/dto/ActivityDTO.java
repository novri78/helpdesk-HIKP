package com.example.helpdesk.backend.dto;

import com.example.helpdesk.backend.model.Ticket;
import com.example.helpdesk.backend.util.TicketStatus;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityDTO {
    private Long id;
    private Date timestamp;
    private String description;
    private TicketStatus ticketStatus;
    private Ticket ticketId;
}
