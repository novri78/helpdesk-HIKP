package com.example.helpdesk.backend.dto;

import com.example.helpdesk.backend.model.Ticket;
import lombok.Data;

import java.util.Date;

@Data
public class ActivityDTO {
    private Long id;
    private Date timestamp;
    private String description;
    private int ticketStatus;
    private Long ticketId;
}
